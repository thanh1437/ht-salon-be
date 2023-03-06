package com.salon.ht.controller;

import com.salon.ht.annotation.CurrentUser;
import com.salon.ht.entity.payload.ApiResponse;
import com.salon.ht.entity.payload.BookingRequest;
import com.salon.ht.entity.payload.BookingResponse;
import com.salon.ht.entity.payload.UpdateBookingResponse;
import com.salon.ht.entity.payload.UpdateStatusListRequest;
import com.salon.ht.entity.payload.WorkingTimeInformationRequest;
import com.salon.ht.security.service.UserDetailsImpl;
import com.salon.ht.service.BookingService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final static Logger LOGGER = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;
    private final RedisTemplate<String, Object> redisTemplate;

    public BookingController(BookingService bookingService, RedisTemplate<String, Object> redisTemplate) {
        this.bookingService = bookingService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER','EMPLOYEE')")
    @ApiOperation(value = "Đặt yêu cầu")
    public ResponseEntity<?> createBooking(@CurrentUser UserDetailsImpl userDetails, @Valid @RequestBody BookingRequest bookingRequest) {
        BookingResponse bookingResponse = bookingService.createBooking(userDetails, bookingRequest);
        LOGGER.info("Booking successfully with booking request {}", bookingRequest);
        return ResponseEntity.ok(bookingResponse);
    }

    @ApiOperation(value = "Cập nhật trạng thái các yêu cầu đặt lịch")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/update-booking-status-list")
    public ResponseEntity<?> updateBookingStatusList(@RequestBody UpdateStatusListRequest updateStatusListRequest) {
        bookingService.updateBookingStatusList(updateStatusListRequest.getIds(), updateStatusListRequest.getStatus());
        return ResponseEntity.ok(new ApiResponse(true, "Cập nhật trạng thái các yêu cầu đặt lịch thành công!"));
    }

    @ApiOperation(value = "Hoàn thành lịch đặt")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/complete-booking/{id}")
    public ResponseEntity<?> completeBooking(@PathVariable("id") Long id, String photo) {
        bookingService.completeBooking(id, photo);
        return ResponseEntity.ok(new ApiResponse(true, "Hoàn thành lịch cắt thành công!"));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @ApiOperation(value = "Tìm kiếm đặt lịch")
    public ResponseEntity<?> getBookings(
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "chooseUserId", required = false) Long chooseUserId,
            @RequestParam(value = "name", required = false) String name
    ) {
        return ResponseEntity.ok(bookingService.getBookings(chooseUserId, name, fromDate, toDate, status, page, pageSize));
    }

    @PostMapping("/get-working-time-information")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @ApiOperation(value = "API lấy thông tin thời gian trống của nhân viên")
    public ResponseEntity<?> getWorkingTimeInformation(@RequestBody WorkingTimeInformationRequest req) {
        return ResponseEntity.ok(bookingService.getWorkingTimeInformation(req.getUserId(), req.getServiceIds(), req.getComboIds(), req.getDate()));
    }
}
