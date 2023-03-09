package com.salon.ht.service;

import com.salon.ht.config.Constant;
import com.salon.ht.dto.PageDto;
import com.salon.ht.dto.ServiceDto;
import com.salon.ht.entity.Booking;
import com.salon.ht.entity.Combo;
import com.salon.ht.entity.ServiceMap;
import com.salon.ht.entity.payload.ComboRequest;
import com.salon.ht.entity.payload.ComboResponse;
import com.salon.ht.entity.payload.ServiceResponse;
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

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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


    public ComboResponse createOrUpdateBooking(UserDetailsImpl userDetails, ComboRequest comboRequest) {
        List<ServiceDto> reqServiceDtos = serviceMapper.toDto(serviceRepository.findByIdIn(comboRequest.getServiceIds()));

        Combo combo;
        if (comboRequest.getId() != null) {
            combo = comboRepository.findById(comboRequest.getId()).orElseThrow(() ->{
                throw new BadRequestException("Combo không tồn tại!");
            });
            combo.setId(comboRequest.getId());
            combo.setModifiedBy(userDetails.getName());
            combo.setModifiedDate(LocalDateTime.now());
        } else {
            combo = new Combo();
            combo.setCode(generateNewComboCode());
            combo.setCreateBy(userDetails.getName());
        }
        combo.setImage(comboRequest.getImage());
        combo.setName(comboRequest.getName());
        combo.setPrice(comboRequest.getPrice());
        combo.setStatus(comboRequest.getStatus());
        combo.setOrderBy(comboRequest.getOrderBy());
        combo = comboRepository.save(combo);
        Long comboId = combo.getId();

        if (comboRequest.getId() != null && !comboRequest.getServiceIds().equals(serviceMapRepository.findServiceIdsByPkId(comboRequest.getId(), Constant.SERVICE_MAP.COMBO.name()))) {
            serviceMapRepository.updateStatusByPkId(comboRequest.getId(), List.of(Constant.SERVICE_MAP.COMBO.name()), 0);
        } else {
            List<ServiceMap> serviceMapList = new ArrayList<>();
            comboRequest.getServiceIds().forEach(serviceId -> {
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

    public PageDto<ComboResponse> getCombos(String name, Integer status, Integer page, Integer pageSize) {
        PageRequest pageRequest;
        if (page == null || pageSize == null) {
            pageRequest = PageRequest.of(0, Integer.MAX_VALUE);
        } else {
            pageRequest = PageRequest.of(page - 1, pageSize);
        }
        Page<Combo> bookings = comboRepositoryImpl.getCombos(name, status, pageRequest);
        return setPageDto(bookings);
    }

    private PageDto<ComboResponse> setPageDto(Page<Combo> comboPage) {
        List<Combo> combos = comboPage.getContent();
        List<ComboResponse> comboResponses = new ArrayList<>();
        List<ServiceDto> serviceDtos = serviceRepositoryImpl.getServiceDtosByPkIdsAndTableName(combos
                .stream().map(Combo::getId).collect(Collectors.toList()), Constant.SERVICE_MAP.COMBO.name());
        for (Combo combo : combos) {
            ComboResponse comboResponse = comboResMapper.toDto(combo);
            comboResponse.setServiceDtos(serviceDtos.stream().filter(serviceDto -> serviceDto.getPkId()
                    .equals(combo.getId())).collect(Collectors.toList()));
            comboResponse.setId(combo.getId());
            comboResponses.add(comboResponse);
        }
        Comparator<ComboResponse> comparator = Comparator
                .comparing(ComboResponse::getStatus, Comparator.reverseOrder())
                .thenComparing(ComboResponse::getOrderBy);
        Collections.sort(comboResponses, comparator);
        return new PageDto<>(comboPage, comboResponses);
    }
}
