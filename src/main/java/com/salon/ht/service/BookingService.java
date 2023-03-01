package com.salon.ht.service;

import com.salon.ht.config.Constant;
import com.salon.ht.constant.BookingStatus;
import com.salon.ht.dto.PageDto;
import com.salon.ht.dto.ServiceDto;
import com.salon.ht.entity.Booking;
import com.salon.ht.entity.ServiceMap;
import com.salon.ht.entity.UserEntity;
import com.salon.ht.entity.payload.BookingRequest;
import com.salon.ht.entity.payload.BookingResponse;
import com.salon.ht.entity.payload.EmailResp;
import com.salon.ht.exception.BadRequestException;
import com.salon.ht.mapper.BookingResMapper;
import com.salon.ht.mapper.ServiceMapper;
import com.salon.ht.repository.BookingRepository;
import com.salon.ht.repository.ServiceMapRepository;
import com.salon.ht.repository.ServiceRepository;
import com.salon.ht.repository.UserRepository;
import com.salon.ht.repository.basic.BookingRepositoryImpl;
import com.salon.ht.repository.basic.ServiceRepositoryImpl;
import com.salon.ht.security.service.UserDetailsImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.salon.ht.common.FnCommon.convertStringToLDT;
import static com.salon.ht.config.Constant.CONTENT_EMAIL_APPROVE;
import static com.salon.ht.config.Constant.DATE_FORMAT;

@Service
@Transactional
public class BookingService extends AbstractService<Booking, Long> {

    private final static Logger LOGGER = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final ServiceRepository serviceRepository;
    private final ServiceMapRepository serviceMapRepository;
    private final ServiceMapper serviceMapper;
    private final BookingResMapper bookingResMapper;
    private final BookingRepositoryImpl bookingRepositoryImpl;
    private final ServiceRepositoryImpl serviceRepositoryImpl;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, ServiceRepository serviceRepository, ServiceMapRepository serviceMapRepository,
                          ServiceMapper serviceMapper, BookingResMapper bookingResMapper, BookingRepositoryImpl bookingRepositoryImpl,
                          ServiceRepositoryImpl serviceRepositoryImpl, EmailService emailService, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.serviceRepository = serviceRepository;
        this.serviceMapRepository = serviceMapRepository;
        this.serviceMapper = serviceMapper;
        this.bookingResMapper = bookingResMapper;
        this.bookingRepositoryImpl = bookingRepositoryImpl;
        this.serviceRepositoryImpl = serviceRepositoryImpl;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    @Override
    protected JpaRepository<Booking, Long> getRepository() {
        return bookingRepository;
    }

    public BookingResponse createOrUpdateBooking(UserDetailsImpl userDetails, BookingRequest request) throws MessagingException {
        if (request.getId() != null && !bookingRepository.existsById(request.getId())) {
            throw new BadRequestException("Lịch đặt không tồn tại");
        }

        List<Long> curentServiceIds = new ArrayList<>();
        LocalDateTime startTime = convertStringToLDT(request.getStartTime());
        List<ServiceDto> reqServiceDtos = new ArrayList<>();
        request.getServiceIds().forEach(id -> {
            reqServiceDtos.add(serviceMapper.toDto(serviceRepository.getById(id)));
        });

        List<Long> reqServiceIds = request.getServiceIds();
        LocalDateTime endTime = startTime.plusMinutes(reqServiceDtos
                .stream().map(ServiceDto::getDuration).reduce(0L, Long::sum));

        Booking booking = new Booking();

        if (request.getId() != null) {
            booking = bookingRepository.getById(request.getId());
            booking.setId(request.getId());
            booking.setModifiedBy(userDetails.getName());
            booking.setModifiedDate(LocalDateTime.now());
            curentServiceIds = serviceMapRepository.findServiceIdsByPkId(request.getId());
        } else {
            booking.setCode(generateNewBookingCode());
        }
        booking.setUserId(userDetails.getId());
        booking.setTitle(String.format("%s %s", userDetails.getName(), DATE_FORMAT.format(startTime)));
        booking.setChooseUserId(request.getChooseUserId());
        booking.setDescription(request.getDescription());
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setBookingStatus(BookingStatus.NEW);
        booking.setTakePhoto(request.getTakePhoto());
        booking.setCreateBy(userDetails.getName());
        booking = bookingRepository.save(booking);
        Long bookingId = booking.getId();

        if (CollectionUtils.isEmpty(curentServiceIds) || !reqServiceIds.containsAll(curentServiceIds) || !curentServiceIds.containsAll(reqServiceIds)
                || reqServiceIds.size() != curentServiceIds.size()) {
            serviceMapRepository.updateStatusByPkId(request.getId(), Constant.SERVICE_MAP.BOOKING.name(), 0);

            List<ServiceMap> serviceMaps = new ArrayList<>();
            reqServiceDtos.forEach(service -> {
                ServiceMap serviceMap = new ServiceMap(bookingId, service.getId(), userDetails.getId(), Constant.SERVICE_MAP.BOOKING.name(), 1);
                serviceMaps.add(serviceMap);
            });
            serviceMapRepository.saveAll(serviceMaps);
        }

        BookingResponse bookingResponse = bookingResMapper.toDto(booking);
        bookingResponse.setServiceDtos(reqServiceDtos);
        bookingResponse.setId(bookingId);
        return bookingResponse;
    }

    private String generateNewBookingCode() {
        Optional<Booking> nearestBooking = bookingRepository.findTopByOrderByIdDesc();
        if (nearestBooking.isEmpty()) {
            return String.format("%s%04d", "B", 1);
        }
        String lastCode = nearestBooking.get().getCode();
        int newCode = Integer.parseInt(lastCode.substring(1)) + 1;
        return String.format("%s%04d", "B", newCode);
    }

    public void updateBookingStatusList(List<Long> bookingIds, Integer status) {

        bookingRepository.updateStatus(bookingIds, status);
        if (status == 3) {
            serviceMapRepository.updateStatusByPkIds(bookingIds, Constant.SERVICE_MAP.BOOKING.name(), 0);
        }
        //send email
        List<EmailResp> emailResps = bookingRepositoryImpl.getEmailRespByBookingIds(bookingIds);
        emailResps.forEach(resp -> {
            if (!StringUtils.isEmpty(resp.getEmail())) {
                try {
                    emailService.sendEmail(resp.getEmail(), Constant.SUBJECT_EMAIL, String.format(status == 3 ?
                                    Constant.CONTENT_EMAIL_REJECT : CONTENT_EMAIL_APPROVE, resp.getUserName(),
                            DATE_FORMAT.format(resp.getStartTime()), DATE_FORMAT.format(resp.getEndTime())));
                } catch (Exception e) {
                    throw new BadRequestException("Có lỗi xảy ra!");
                }
            }

        });

    }

    public void completeBooking(Long id, String photo) {
        if (!bookingRepository.existsById(id)) {
            throw new BadRequestException("Lịch đặt không tồn tại!");
        }
        Booking booking = bookingRepository.getById(id);
        if (booking.getTakePhoto() == 1 && StringUtils.isEmpty(photo)) {
            throw new BadRequestException("Vui lòng tải ảnh vì khách hàng đã yêu cầu trước đó!");
        }
        booking.setPhoto(photo);
        booking.setBookingStatus(BookingStatus.FINISHED);
        bookingRepository.save(booking);
    }

    public PageDto<BookingResponse> getBookings(String title, String code, Long chooseUserId, Long userId,
                                                String fromDate, String toDate, Integer status, Integer page, Integer pageSize) {
        PageRequest pageRequest;
        if (page == null || pageSize == null) {
            pageRequest = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.ASC, "status");
        } else {
            pageRequest = PageRequest.of(page - 1, pageSize, Sort.Direction.ASC, "status");
        }
        Page<Booking> bookings = bookingRepositoryImpl.getBooking(title, code, chooseUserId, userId, fromDate, toDate, status, pageRequest);
        return setPageDto(bookings);
    }

    private PageDto<BookingResponse> setPageDto(Page<Booking> bookingPage) {
        List<Booking> bookings = bookingPage.getContent();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        List<ServiceDto> serviceDtos = serviceRepositoryImpl.getServiceDtosByPkIdsAndTableName(bookings
                .stream().map(Booking::getId).collect(Collectors.toList()), Constant.SERVICE_MAP.BOOKING.name());
        for (Booking booking : bookings) {
            BookingResponse bookingResponse = new BookingResponse();
            BeanUtils.copyProperties(booking, bookingResponse);
            bookingResponse.setServiceDtos(serviceDtos.stream().filter(serviceDto -> serviceDto.getPkId()
                    .equals(booking.getId())).collect(Collectors.toList()));
            bookingResponse.setId(booking.getId());
            bookingResponses.add(bookingResponse);
        }
        bookingResponses.sort(Comparator.comparing(BookingResponse::getBookingStatus));
        return new PageDto<>(bookingPage, bookingResponses);
    }

}
