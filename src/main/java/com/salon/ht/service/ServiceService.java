package com.salon.ht.service;

import com.salon.ht.dto.PageDto;
import com.salon.ht.entity.Service;
import com.salon.ht.entity.payload.ServiceRequest;
import com.salon.ht.entity.payload.ServiceResponse;
import com.salon.ht.exception.BadRequestException;
import com.salon.ht.mapper.ServiceResMapper;
import com.salon.ht.repository.ServiceRepository;
import com.salon.ht.repository.basic.ServiceRepositoryImpl;
import com.salon.ht.security.service.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@Transactional
public class ServiceService {
    private final ServiceRepository serviceRepository;
    private final ServiceResMapper serviceResMapper;
    private final ServiceRepositoryImpl serviceRepositoryImpl;

    public ServiceService(ServiceRepository serviceRepository, ServiceResMapper serviceResMapper, ServiceRepositoryImpl serviceRepositoryImpl) {
        this.serviceRepository = serviceRepository;
        this.serviceResMapper = serviceResMapper;
        this.serviceRepositoryImpl = serviceRepositoryImpl;
    }

    public ServiceResponse createOrUpdateService(UserDetailsImpl userDetails, ServiceRequest serviceRequest) {
        Service service = new Service();
        if (serviceRequest.getId() != null) {
            service = serviceRepository.getById(serviceRequest.getId());
            service.setModifiedBy(userDetails.getName());
            service.setModifiedDate(LocalDateTime.now());
        } else {
            service.setCreateBy(userDetails.getName());
            service.setCode(generateNewServiceCode());
        }
        service.setImage(serviceRequest.getImage());
        service.setName(serviceRequest.getName());
        service.setOrderBy(serviceRequest.getOrderBy());
        service.setPrice(serviceRequest.getPrice());
        service.setDuration(serviceRequest.getDuration());
        service.setStatus(serviceRequest.getStatus());
        service = serviceRepository.save(service);
        return serviceResMapper.toDto(service);
    }

    private String generateNewServiceCode() {
        Optional<Service> nearestService = serviceRepository.findTopByOrderByIdDesc();
        if (nearestService.isEmpty()) {
            return String.format("%s%04d", "S", 1);
        }
        String lastCode = nearestService.get().getCode();
        int newCode = Integer.parseInt(lastCode.substring(1)) + 1;
        return String.format("%s%04d", "S", newCode);
    }

    public void updateServiceStatusList(List<Long> ids, Integer status) {
        try {
            serviceRepository.updateStatus(ids, status);
        } catch (Exception e) {
            throw new BadRequestException("Có lỗi xảy ra!");
        }
    }

    public PageDto<ServiceResponse> getServices(String name, String code, Integer page, Integer pageSize) {
        PageRequest pageRequest;
        if (page == null || pageSize == null) {
            pageRequest = PageRequest.of(0, Integer.MAX_VALUE);
        } else {
            pageRequest = PageRequest.of(page - 1, pageSize);
        }
        Page<Service> services = serviceRepositoryImpl.getServices(name, code, pageRequest);
        List<ServiceResponse> serviceResponses = serviceResMapper.toDto(services.getContent());

        Comparator<ServiceResponse> comparator = Comparator
                .comparing(ServiceResponse::getStatus, Comparator.reverseOrder())
                .thenComparing(ServiceResponse::getOrderBy);
        Collections.sort(serviceResponses, comparator);
        return new PageDto<>(services, serviceResponses);
    }
}
