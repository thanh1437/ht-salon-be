package com.salon.ht.service;

import com.salon.ht.config.Constant;
import com.salon.ht.dto.ComboDto;
import com.salon.ht.dto.PageDto;
import com.salon.ht.dto.ServiceDto;
import com.salon.ht.entity.Booking;
import com.salon.ht.entity.Combo;
import com.salon.ht.entity.Service;
import com.salon.ht.entity.ServiceMap;
import com.salon.ht.entity.UserEntity;
import com.salon.ht.entity.payload.BookingRequest;
import com.salon.ht.entity.payload.BookingResponse;
import com.salon.ht.exception.BadRequestException;
import com.salon.ht.mapper.BookingResMapper;
import com.salon.ht.mapper.ComboMapper;
import com.salon.ht.mapper.ServiceMapper;
import com.salon.ht.repository.BookingRepository;
import com.salon.ht.repository.ComboRepository;
import com.salon.ht.repository.ServiceMapRepository;
import com.salon.ht.repository.ServiceRepository;
import com.salon.ht.repository.UserRepository;
import com.salon.ht.repository.basic.BookingRepositoryImpl;
import com.salon.ht.repository.basic.ServiceRepositoryImpl;
import com.salon.ht.security.service.UserDetailsImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.salon.ht.common.FnCommon.convertStringToLDT;
import static com.salon.ht.config.Constant.DATE_TIME_FORMATTER;

@org.springframework.stereotype.Service
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
    private final RoleService roleService;
    private final ComboRepository comboRepository;
    private final ComboMapper comboMapper;
    private final UserService userService;

    public BookingService(BookingRepository bookingRepository, ServiceRepository serviceRepository, ServiceMapRepository serviceMapRepository,
                          ServiceMapper serviceMapper, BookingResMapper bookingResMapper, BookingRepositoryImpl bookingRepositoryImpl,
                          ServiceRepositoryImpl serviceRepositoryImpl, EmailService emailService, UserRepository userRepository,
                          RoleService roleService, ComboRepository comboRepository, ComboMapper comboMapper, UserService userService) {
        this.bookingRepository = bookingRepository;
        this.serviceRepository = serviceRepository;
        this.serviceMapRepository = serviceMapRepository;
        this.serviceMapper = serviceMapper;
        this.bookingResMapper = bookingResMapper;
        this.bookingRepositoryImpl = bookingRepositoryImpl;
        this.serviceRepositoryImpl = serviceRepositoryImpl;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.comboRepository = comboRepository;
        this.comboMapper = comboMapper;
        this.userService = userService;
    }

    @Override
    protected JpaRepository<Booking, Long> getRepository() {
        return bookingRepository;
    }

    public BookingResponse createBooking(UserDetailsImpl userDetails, BookingRequest request) {
        LocalDateTime startTime = convertStringToLDT(request.getStartTime());
        LocalDateTime endTime = caculateEndTime(startTime, request);
        List<ServiceDto> reqServiceDtos = getServiceDtoFromBookingRequest(request);
        Long totalPrice = getTotalPrice(request.getComboIds(), reqServiceDtos);

        Booking booking;
        if (request.getId() != null) {
            booking = bookingRepository.findById(request.getId()).orElseThrow(() -> {
                throw new BadRequestException("Yêu cầu không tồn tại!");
            });
            booking.setModifiedBy(userDetails.getName());
            booking.setModifiedDate(LocalDateTime.now());
            serviceMapRepository.updateStatusByPkId(request.getId(), List.of(Constant.SERVICE_MAP.BOOKING_COMBO.name(),
                    Constant.SERVICE_MAP.BOOKING.name()), 0);
        } else {
            booking = new Booking();
            booking.setCode(generateNewBookingCode());
            booking.setCreateBy(userDetails.getName());
            booking.setUserId(userDetails.getId());
        }
        booking.setTitle(String.format("%s %s", userDetails.getName(), DATE_TIME_FORMATTER.format(startTime)));
        booking.setChooseUserId(request.getChooseUserId());
        booking.setDescription(request.getDescription());
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setStatus(0);
        booking.setTakePhoto(request.getTakePhoto());
        booking = bookingRepository.save(booking);
        request.setId(booking.getId());

        changeDataBooking(userDetails, request, reqServiceDtos);

        BookingResponse bookingResponse = bookingResMapper.toDto(booking);
        if (!CollectionUtils.isEmpty(reqServiceDtos)) {
            bookingResponse.setServiceDtos(reqServiceDtos);
        }
        if (!CollectionUtils.isEmpty(request.getComboIds())) {
            bookingResponse.setComboDtos(comboMapper.toDto(comboRepository.findByIdIn(request.getComboIds())));
        }
        bookingResponse.setId(booking.getId());
        bookingResponse.setTotalPrice(totalPrice);
        UserEntity userEntity = userRepository.getById(request.getChooseUserId());
        bookingResponse.setChooseUser(String.format("%s - %s", userEntity.getName(), userEntity.getCode()));
        return bookingResponse;
    }

    private Long getTotalPrice(List<Long> comboIds, List<ServiceDto> reqServiceDtos) {
        Long totalPrice = 0L;

        if (!CollectionUtils.isEmpty(reqServiceDtos)) {
            totalPrice += reqServiceDtos
                    .stream().map(ServiceDto::getPrice).reduce(0L, Long::sum);
        }
        if (!CollectionUtils.isEmpty(comboIds)) {
            totalPrice += comboRepository.findByIdIn(comboIds).stream().map(Combo::getPrice).reduce(0L, Long::sum);
        }
        return totalPrice;
    }

    private void changeDataBooking(UserDetailsImpl userDetails, BookingRequest request, List<ServiceDto> reqServiceDtos) {
        List<ServiceMap> serviceMaps = new ArrayList<>();
        if (!CollectionUtils.isEmpty(reqServiceDtos)) {
            reqServiceDtos.forEach(service -> {
                ServiceMap serviceMap = new ServiceMap(request.getId(), service.getId(), null, userDetails.getId(), Constant.SERVICE_MAP.BOOKING.name(), 1);
                serviceMaps.add(serviceMap);
            });
        }
        if (!CollectionUtils.isEmpty(request.getComboIds())) {
            request.getComboIds().forEach(comboId -> {
                ServiceMap serviceMap = new ServiceMap(request.getId(), null, comboId, userDetails.getId(), Constant.SERVICE_MAP.BOOKING_COMBO.name(), 1);
                serviceMaps.add(serviceMap);
            });
        }
        serviceMapRepository.saveAll(serviceMaps);
    }

    private List<ServiceDto> getServiceDtoFromBookingRequest(BookingRequest request) {
        List<ServiceDto> reqServiceDtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getServiceIds())) {
            request.getServiceIds().forEach(id -> {
                reqServiceDtos.add(serviceMapper.toDto(serviceRepository.getById(id)));
            });
        }
        return reqServiceDtos;
    }

    private LocalDateTime caculateEndTime(LocalDateTime startTime, BookingRequest request) {
        List<ServiceDto> reqServiceDtos = new ArrayList<>();
        List<ServiceDto> serviceCalDtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getServiceIds())) {
            request.getServiceIds().forEach(id -> {
                reqServiceDtos.add(serviceMapper.toDto(serviceRepository.getById(id)));
            });
            serviceCalDtos.addAll(reqServiceDtos);
        }
        if (!CollectionUtils.isEmpty(request.getComboIds())) {
            List<Service> services = serviceRepository.findByComboIds(request.getComboIds());
            serviceCalDtos.addAll(serviceMapper.toDto(services));
        }

        return startTime.plusMinutes(serviceCalDtos
                .stream().map(ServiceDto::getDuration).reduce(0L, Long::sum));
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
//        List<EmailResp> emailResps = bookingRepositoryImpl.getEmailRespByBookingIds(bookingIds);
//        emailResps.forEach(resp -> {
//            if (!StringUtils.isEmpty(resp.getEmail())) {
//                try {
//                    emailService.sendEmail(resp.getEmail(), Constant.SUBJECT_EMAIL, String.format(status == 3 ?
//                                    Constant.CONTENT_EMAIL_REJECT : CONTENT_EMAIL_APPROVE, resp.getUserName(),
//                            DATE_TIME_FORMATTER.format(resp.getStartTime()), DATE_TIME_FORMATTER.format(resp.getEndTime())));
//                } catch (Exception e) {
//                    throw new BadRequestException("Có lỗi xảy ra!");
//                }
//            }
//
//        });

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
        booking.setStatus(2);
        bookingRepository.save(booking);
    }

    public PageDto<BookingResponse> getBookings(UserDetailsImpl userDetails, Long chooseUserId, String name, String fromDate, String toDate, Integer status, Integer page, Integer pageSize) {
        PageRequest pageRequest;
        if (page == null || pageSize == null) {
            pageRequest = PageRequest.of(0, Integer.MAX_VALUE);
        } else {
            pageRequest = PageRequest.of(page - 1, pageSize);
        }
        Page<Booking> bookings = bookingRepositoryImpl.getBooking(userDetails, chooseUserId, name, fromDate, toDate, status, pageRequest);
        return setPageDto(bookings);
    }

    private PageDto<BookingResponse> setPageDto(Page<Booking> bookingPage) {
        List<Booking> bookings = bookingPage.getContent();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        List<ServiceDto> serviceDtos = serviceRepositoryImpl.getServiceDtosByPkIdsAndTableName(bookings
                .stream().map(Booking::getId).collect(Collectors.toList()), Constant.SERVICE_MAP.BOOKING.name());
        List<ComboDto> comboDtos = serviceRepositoryImpl.getComboDtosByPkIdsAndTableName(bookings
                .stream().map(Booking::getId).collect(Collectors.toList()), Constant.SERVICE_MAP.BOOKING_COMBO.name());
        for (Booking booking : bookings) {
            BookingResponse bookingResponse = bookingResMapper.toDto(booking);
            bookingResponse.setServiceDtos(serviceDtos.stream().filter(serviceDto -> serviceDto.getPkId()
                    .equals(booking.getId())).collect(Collectors.toList()));
            bookingResponse.setComboDtos(comboDtos.stream().filter(serviceDto -> serviceDto.getPkId()
                    .equals(booking.getId())).collect(Collectors.toList()));

            bookingResponse.setTotalPrice(getTotalPrice(comboDtos.stream().map(ComboDto::getId).collect(Collectors.toList()), serviceDtos));
            UserEntity userEntity = userRepository.getById(booking.getChooseUserId());
            bookingResponse.setChooseUser(String.format("%s - %s", userEntity.getName(), userEntity.getCode()));
            bookingResponses.add(bookingResponse);
        }
        bookingResponses.sort(Comparator.comparing(BookingResponse::getStatus));
        return new PageDto<>(bookingPage, bookingResponses);
    }

    public List<String> getWorkingTimeInformation(Long userId, List<Long> serviceIds, List<Long> comboIds, String date) {
        //cal duration
        long duration = 0L;
        List<ServiceDto> serviceDtos = new ArrayList<>();

        if (!CollectionUtils.isEmpty(serviceIds)) {
            serviceIds.forEach(id -> {
                serviceDtos.add(serviceMapper.toDto(serviceRepository.getById(id)));
            });
        }
        if (!CollectionUtils.isEmpty(comboIds)) {
            List<Service> services = serviceRepository.findByComboIds(comboIds);
            serviceDtos.addAll(serviceMapper.toDto(services));
        }

        if (!CollectionUtils.isEmpty(serviceDtos)) {
            duration = serviceDtos.stream().map(ServiceDto::getDuration).reduce(0L, Long::sum);
        }
        //get mapDate
        Map<LocalTime, LocalTime> mapTime = new HashMap<>();
        List<Booking> bookings = bookingRepository.findByUserChooseIds(userId, date);
        bookings.forEach(booking -> {
            mapTime.put(booking.getStartTime().toLocalTime(), booking.getEndTime().toLocalTime());
        });
        return getHourList(mapTime, duration);
    }

    public static List<String> getHourList(Map<LocalTime, LocalTime> mapTime, Long duration) {
        List<LocalTime> hourList = new ArrayList<>();
        // Duyệt qua từng giờ và thêm vào danh sách nếu không thuộc bất kỳ khoảng thời gian nào trong danh sách
        for (int hour = 7; hour < 23; hour++) {
            LocalTime time = LocalTime.of(hour, 0);
            boolean shouldExclude = false;
            for (Map.Entry<LocalTime, LocalTime> entry : mapTime.entrySet()) {
                if (time.plusMinutes(duration).isAfter(entry.getKey()) && time.isBefore(entry.getValue())) {
                    shouldExclude = true;
                    break;
                }
            }
            if (!shouldExclude) {
                hourList.add(time);
            }
        }
        return hourList.stream().map(Constant.TIME_FORMATTER::format).collect(Collectors.toList());
    }

}
