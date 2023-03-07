package com.salon.ht.entity.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "Yêu cầu đăng ký người dùng", description = "Payload dùng để đăng ký người dùng mới")
public class RegistrationUserRequest implements Serializable {
    @ApiModelProperty(value = "Id người dùng", dataType = "Long", required = true)
    @NotNull(message = "Id người dùng không được trống")
    private Long id;

    @ApiModelProperty(value = "Tên tài khoàn người dùng", dataType = "String", required = true)
    @NotNull(message = "Tên tài khoàn người dùng không được thiếu")
    private String username;

    @ApiModelProperty(value = "Mật khẩu người dùng", dataType = "String")
    private String password;

    @ApiModelProperty(value = "Họ và tên người dùng", dataType = "String", required = true)
    @NotNull(message = "Họ và tên người dùng không được trống")
    private String name;

    @ApiModelProperty(value = "Số điện thoại người dùng", dataType = "String", required = true)
    @NotNull(message = "Số điện thoại người dùng không được trống")
    private String mobile;

    @ApiModelProperty(value = "Email người dùng", dataType = "String", required = true)
    @NotNull(message = "Email người dùng không được trống")
    private String email;

    @ApiModelProperty(value = "Id quyền", dataType = "String", required = true)
    @NotNull(message = "Quyền không được trống")
    private String role;

    @ApiModelProperty(value = "Ảnh người dùng", dataType = "String", required = true)
    @NotEmpty(message = "Ảnh người dùng không thể bỏ trống")
    private String photo;
}
