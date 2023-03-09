package com.salon.ht.controller;

import com.salon.ht.annotation.CurrentUser;
import com.salon.ht.entity.payload.ServiceRequest;
import com.salon.ht.entity.payload.ServiceResponse;
import com.salon.ht.security.service.UserDetailsImpl;
import com.salon.ht.service.ServiceService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/service")
public class ServiceController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ServiceController.class);

    private final ServiceService serviceService;


    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER','EMPLOYEE')")
    @ApiOperation(value = "Tạo service")
    public ResponseEntity<?> createOrUpdateService(@CurrentUser UserDetailsImpl userDetails, @Valid @RequestBody ServiceRequest serviceRequest) {
        ServiceResponse serviceResponse = serviceService.createOrUpdateService(userDetails, serviceRequest);
        LOGGER.info("Booking successfully with booking request {}", serviceRequest);
        return ResponseEntity.ok(serviceResponse);
    }

    @GetMapping("/search")
    @ApiOperation(value = "Tìm kiếm dịch vụ")
    public ResponseEntity<?> getServices(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        return ResponseEntity.ok(serviceService.getServices(name, code, status, page, pageSize));
    }

}
