package com.salon.ht.controller;

import com.salon.ht.annotation.CurrentUser;
import com.salon.ht.entity.payload.ApiResponse;
import com.salon.ht.entity.payload.ComboRequest;
import com.salon.ht.entity.payload.ComboResponse;
import com.salon.ht.entity.payload.UpdateStatusListRequest;
import com.salon.ht.security.service.UserDetailsImpl;
import com.salon.ht.service.ComboService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/combo")
public class ComboController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ComboController.class);

    private final ComboService comboService;
    private final RedisTemplate<String, Object> redisTemplate;


    public ComboController(ComboService comboService, RedisTemplate<String, Object> redisTemplate) {
        this.comboService = comboService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER','EMPLOYEE')")
    @ApiOperation(value = "Tạo combo")
    public ResponseEntity<?> createOrUpdateCombo(@CurrentUser UserDetailsImpl userDetails, @Valid @RequestBody ComboRequest comboRequest) {
        ComboResponse bookingResponse = comboService.createOrUpdateBooking(userDetails, comboRequest);
        LOGGER.info("Booking successfully with booking request {}", comboRequest);
        return ResponseEntity.ok(bookingResponse);
    }

    @GetMapping("/search")
    @ApiOperation(value = "Tìm kiếm combo")
    public ResponseEntity<?> getCombos(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        return ResponseEntity.ok(comboService.getCombos(name, status, page, pageSize));
    }

}
