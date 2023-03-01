package com.salon.ht.controller;

import com.salon.ht.entity.payload.ApiResponse;
import com.salon.ht.entity.payload.RoleRequest;
import com.salon.ht.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/roles")
@Api(tags = "RoleService", description = "Định nghĩa các quyền của người dùng, quản lý phân quyền người dùng")
public class RoleController {

    @Autowired
    private RoleService roleService;

    public RoleController() {}

    @ApiOperation(value = "API lấy danh sách quyền")
    @GetMapping
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @ApiOperation(value = "Api để lấy thông tin chi tiết của Role")
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleDetail(@PathVariable("id") String id) {
        Long roleId = Long.parseLong(id);
        return ResponseEntity.ok(roleService.getRoleDetail(roleId));
    }

    @ApiOperation(value = "API để xóa quyền")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable("id") String id) {
        Long roleId = Long.parseLong(id);
        roleService.deleteRole(roleId);
        return ResponseEntity.ok(new ApiResponse(true, "Xóa quyền với id " + id + " thành công!"));
    }

    @ApiOperation(value = "API để thêm mới quyền")
    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody RoleRequest request) {
        roleService.createRole(request);
        return ResponseEntity.ok(new ApiResponse(true, "Thêm mới quyền thành công!"));
    }

    @ApiOperation(value = "API để cập nhật thông tin quyền")
    @PutMapping()
    public ResponseEntity<?> updateRole(@RequestBody RoleRequest roleRequest) {
        roleService.updateRole(roleRequest);
        return ResponseEntity.ok(new ApiResponse(true, "Cập nhật quyền thành công!"));
    }

}
