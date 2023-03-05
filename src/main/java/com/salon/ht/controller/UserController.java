package com.salon.ht.controller;

import com.google.gson.Gson;
import com.salon.ht.annotation.CurrentUser;
import com.salon.ht.entity.payload.ApiResponse;
import com.salon.ht.entity.payload.RegistrationUserRequest;
import com.salon.ht.exception.BadRequestException;
import com.salon.ht.security.service.UserDetailsImpl;
import com.salon.ht.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Api(tags = "UserService", description = "Định nghĩa các API lên quan đến người dùng trong hệ thống")
public class UserController {

    private final UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final Gson gson;

    public UserController(
            UserService userService, Gson gson) {
        this.userService = userService;
        this.gson = gson;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @ApiOperation(value = "API lấy thông tin người dùng theo id người dùng")
    public ResponseEntity<?> getUser(@PathVariable("id") Long userId) {
        LOGGER.info("Getting user with id {}", userId);
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @ApiOperation(value = "API lấy thông tin người dùng vào hệ thống")
    public ResponseEntity<?> getUserProfile(@CurrentUser UserDetailsImpl currentUser) {
        LOGGER.info(currentUser.getUsername() + " has role: " + currentUser.getRoles());
        return ResponseEntity.ok(userService.getUser(currentUser.getId()));
    }

    @GetMapping
    @ApiOperation(value = "APi lấy danh sách người dùng hệ thống")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> getUsers(@RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "status", required = false) Integer status,
                                      @RequestParam(value = "email", required = false) String email,
                                      @RequestParam(value = "mobile", required = false) String mobile,
                                      @RequestParam(value = "departmentId", required = false) Long departmentId,
                                      @RequestParam(value = "page", required = false) Integer page,
                                      @RequestParam(value = "fromDate", required = false) String fromDate,
                                      @RequestParam(value = "toDate", required = false) String toDate,
                                      @RequestParam(value = "limit", required = false) Integer limit) {
        return new ResponseEntity<>(userService.getList(name, status, mobile, email, departmentId, fromDate, toDate, page, limit), HttpStatus.OK);
    }

    @PostMapping()
    @ApiOperation(value = "API tạo mới người dùng hệ thống", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@Valid @RequestPart("user") String user) {
        LOGGER.info("User create req {}", user);
        RegistrationUserRequest request;
        try {
            request = gson.fromJson(user, RegistrationUserRequest.class);
        } catch (Exception e) {
            throw new BadRequestException("Không thể đọc được dữ liệu user truyền lên. Hãy xem lại các trường dữ liệu truyền lên");
        }
        userService.createUser(request);
        return ResponseEntity.ok(new ApiResponse(true, "Thêm mới nhân viên vào hệ thống thành công!"));
    }


    @ApiOperation(value = "API xóa người dùng khỏi hệ thống")
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse(true, "Xoá nhân viên thành công!"));
    }

    @ApiOperation(value = "API xóa người dùng khỏi hệ thống")
    @PatchMapping("/update-status/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStatus(@PathVariable("userId") Long userId, @RequestBody Map<String, Object> request) {
        userService.updateStatus(userId, request);
        return ResponseEntity.ok(new ApiResponse(true, "Cập nhật trạng thái người dùng thành công"));
    }

    @PostMapping("/change_password")
    @ApiOperation(value = "API đổi mật khẩu người dùng")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> changePassword(@CurrentUser UserDetailsImpl currentUser, @RequestBody Map<String, Object> request) {
        String oldPassword = (String) request.get("oldPassword");
        String newPassword = (String) request.get("newPassword");
        String confirmNewPassword = (String) request.get("confirmNewPassword");

        userService.changePassword(currentUser, oldPassword, newPassword, confirmNewPassword);

        return ResponseEntity.ok(new ApiResponse(true, "Đổi mật khẩu thành công!"));
    }

    @PostMapping("/reset_password/{userId}")
    @ApiOperation(value = "API dành cho admin đổi mật khẩu người dùng")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> resetPassword(@PathVariable("userId") Long userId, @RequestBody Map<String, Object> request) {
        String newPassword = (String) request.get("newPassword");
        String confirmNewPassword = (String) request.get("confirmNewPassword");

        userService.resetPassword(userId, newPassword, confirmNewPassword);

        return ResponseEntity.ok(new ApiResponse(true, "Cập nhật mật khẩu người dùng thành công!"));
    }

    @PostMapping("/update")
    @ApiOperation(value = "API để thay đổi thông tin của nhân viên")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@RequestPart(value = "photo", required = false) MultipartFile photo,
                                        @Valid @RequestParam(value = "user", required = false) String user) {
        LOGGER.info("update user request {}", user);
        RegistrationUserRequest request;
        try {
            request = gson.fromJson(user, RegistrationUserRequest.class);
        } catch (Exception e) {
            throw new BadRequestException("Không thể đọc được dữ liệu user truyền lên. Hãy xem lại các trường dữ liệu truyền lên");
        }
        userService.updateUser(request, photo);
        return ResponseEntity.ok(new ApiResponse(true, "Cập nhật thông tin nhân viên thành công!"));
    }

    @PostMapping("/import_csv")
    @ApiOperation(value = "API để import dữ liệu người dùng")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> importUserByCSV(@RequestPart(value = "file") MultipartFile file) {
        String message = userService.importUserByCSV(file);
        return ResponseEntity.ok(new ApiResponse(true, message));
    }

    @GetMapping("/get-users-role-employee")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @ApiOperation(value = "API danh sách các thợ cắt tóc")
    public ResponseEntity<?> getUsersRoleEmployee() {
        return ResponseEntity.ok(userService.getUsersRoleEmployee());
    }
}
