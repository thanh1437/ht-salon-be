package com.salon.ht.controller;

import com.salon.ht.entity.payload.ApiResponse;
import com.salon.ht.entity.payload.DepartmentRequest;
import com.salon.ht.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/departments")
@Api(tags = "DepartmentService", description = "Định nghĩa các danh sách API về phòng ban của người dùng")
@PreAuthorize("hasRole('ADMIN') and hasAuthority('manager_dept')")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    @ApiOperation(value = "Trả về danh sách phòng ban")
    public ResponseEntity<?> getDepartments(@RequestParam(name = "status", defaultValue = "true") boolean status,
                                            @RequestParam(name = "name", required = false) String name) {
        return ResponseEntity.ok(departmentService.getList(name, status));
    }

    @PostMapping
    @ApiOperation(value = "Tạo mới phòng ban phòng ban")
    public ResponseEntity<?> createDepartment(@Valid @RequestBody DepartmentRequest departmentRequest) {
        LOGGER.info("Creat department with request {}", departmentRequest);
        departmentService.createDepartment(departmentRequest);
        return ResponseEntity.ok(new ApiResponse(true, "Tạo phòng ban thành công !"));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Xóa phòng ban")
    public ResponseEntity<?> deleteDepartment(@PathVariable("id") Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(new ApiResponse(true, "Xóa phòng ban thành công !"));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Cập nhật thông tin phòng ban")
    public ResponseEntity<?> updateDepartment(@PathVariable("id") Long id, @Valid @RequestBody DepartmentRequest departmentRequest) {
        departmentService.updateDepartment(departmentRequest);
        return ResponseEntity.ok(new ApiResponse(true, "Cập nhật thông tin phòng ban thành công"));
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);
}
