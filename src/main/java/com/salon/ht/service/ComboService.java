package com.salon.ht.service;

import com.salon.ht.config.Constant;
import com.salon.ht.dto.PageDto;
import com.salon.ht.dto.ServiceDto;
import com.salon.ht.entity.Booking;
import com.salon.ht.entity.Combo;
import com.salon.ht.entity.ServiceMap;
import com.salon.ht.entity.payload.ComboRequest;
import com.salon.ht.entity.payload.ComboResponse;
import com.salon.ht.exception.BadRequestException;
import com.salon.ht.mapper.ComboResMapper;
import com.salon.ht.mapper.ServiceMapper;
import com.salon.ht.repository.ComboRepository;
import com.salon.ht.repository.ServiceMapRepository;
import com.salon.ht.repository.ServiceRepository;
import com.salon.ht.repository.basic.ComboRepositoryImpl;
import com.salon.ht.repository.basic.ServiceRepositoryImpl;
import com.salon.ht.security.service.UserDetailsImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ComboService {

    private final ComboRepository comboRepository;
    private final ServiceRepository serviceRepository;
    private final ServiceMapRepository serviceMapRepository;
    private final ServiceMapper serviceMapper;
    private final ComboRepositoryImpl comboRepositoryImpl;
    private final ServiceRepositoryImpl serviceRepositoryImpl;
    private final ComboResMapper comboResMapper;

    public ComboService(ComboRepository comboRepository, ServiceRepository serviceRepository, ServiceMapRepository serviceMapRepository, ServiceMapper serviceMapper,
                        ComboRepositoryImpl comboRepositoryImpl, ServiceRepositoryImpl serviceRepositoryImpl, ComboResMapper comboResMapper) {
        this.comboRepository = comboRepository;
        this.serviceRepository = serviceRepository;
        this.serviceMapRepository = serviceMapRepository;
        this.serviceMapper = serviceMapper;
        this.comboRepositoryImpl = comboRepositoryImpl;
        this.serviceRepositoryImpl = serviceRepositoryImpl;
        this.comboResMapper = comboResMapper;
    }


    public ComboResponse createOrUpdateBooking(UserDetailsImpl userDetails, ComboRequest comboDto) {
        if (CollectionUtils.isEmpty(comboDto.getServiceIds())) {
            throw new BadRequestException("Danh sách dịch vụ không được bỏ trống");
        }

        List<ServiceDto> reqServiceDtos = new ArrayList<>();
        comboDto.getServiceIds().forEach(id -> {
            reqServiceDtos.add(serviceMapper.toDto(serviceRepository.getById(id)));
        });
        List<Long> reqServiceIds = comboDto.getServiceIds();
        List<Long> currentCmsIds = new ArrayList<>();
        Combo combo = new Combo();
        if (comboDto.getId() != null) {
            combo = comboRepository.getById(comboDto.getId());
            combo.setId(comboDto.getId());
            combo.setModifiedBy(userDetails.getName());
            combo.setModifiedDate(LocalDateTime.now());
            currentCmsIds = serviceMapRepository.findServiceIdsByPkId(comboDto.getId());
        } else {
            combo.setCode(generateNewComboCode());
        }
        combo.setName(comboDto.getName());
        combo.setPrice(comboDto.getPrice());
        combo.setStatus(comboDto.getStatus());
        combo.setCreateBy(userDetails.getName());
        combo = comboRepository.save(combo);
        Long comboId = combo.getId();

        if (CollectionUtils.isEmpty(currentCmsIds) || !reqServiceIds.containsAll(currentCmsIds) || !currentCmsIds.containsAll(reqServiceIds)
                || reqServiceIds.size() != currentCmsIds.size()) {
            serviceMapRepository.updateStatusByPkId(comboId, Constant.SERVICE_MAP.COMBO.name(), 0);
            List<ServiceMap> serviceMapList = new ArrayList<>();
            reqServiceIds.forEach(serviceId -> {
                ServiceMap serviceMap = new ServiceMap(comboId, serviceId, comboId, userDetails.getId(), Constant.SERVICE_MAP.COMBO.name(), 1);
                serviceMapList.add(serviceMap);
            });
            serviceMapRepository.saveAll(serviceMapList);
        }

        ComboResponse comboResponse = comboResMapper.toDto(combo);
        BeanUtils.copyProperties(combo, comboResponse);
        comboResponse.setServiceDtos(reqServiceDtos);
        comboResponse.setId(comboId);
        return comboResponse;
    }

    private String generateNewComboCode() {
        Optional<Combo> nearestBooking = comboRepository.findTopByOrderByIdDesc();
        if (nearestBooking.isEmpty()) {
            return String.format("%s%04d", "C", 1);
        }
        String lastCode = nearestBooking.get().getCode();
        int newCode = Integer.parseInt(lastCode.substring(1)) + 1;
        return String.format("%s%04d", "C", newCode);
    }

    public void updateComboStatusList(List<Long> comboIds, Integer status) {
        try {
            comboRepository.updateStatus(comboIds, status);
        } catch (Exception e) {
            throw new BadRequestException("Có lỗi xảy ra!");
        }
    }

    public PageDto<ComboResponse> getCombos(String name, String code, String fromDate, String toDate, Integer status, Integer page, Integer pageSize) {
        PageRequest pageRequest;
        if (page == null || pageSize == null) {
            pageRequest = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.ASC, "status");
        } else {
            pageRequest = PageRequest.of(page - 1, pageSize, Sort.Direction.ASC, "status");
        }
        Page<Combo> bookings = comboRepositoryImpl.getCombos(name, code, fromDate, toDate, status, pageRequest);
        return setPageDto(bookings);
    }

    private PageDto<ComboResponse> setPageDto(Page<Combo> comboPage) {
        List<Combo> combos = comboPage.getContent();
        List<ComboResponse> comboResponses = new ArrayList<>();
        List<ServiceDto> serviceDtos = serviceRepositoryImpl.getServiceDtosByPkIdsAndTableName(combos
                .stream().map(Combo::getId).collect(Collectors.toList()), Constant.SERVICE_MAP.COMBO.name());
        for (Combo combo : combos) {
            ComboResponse comboResponse = new ComboResponse();
            BeanUtils.copyProperties(combo, comboResponse);
            comboResponse.setServiceDtos(serviceDtos.stream().filter(serviceDto -> serviceDto.getPkId()
                    .equals(combo.getId())).collect(Collectors.toList()));
            comboResponse.setId(combo.getId());
            comboResponses.add(comboResponse);
        }
        comboResponses.sort(Comparator.comparing(ComboResponse::getStatus));
        return new PageDto<>(comboPage, comboResponses);
    }
}
