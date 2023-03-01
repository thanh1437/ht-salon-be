package com.salon.ht.entity.payload;

import com.salon.ht.validation.annotation.NullOrNotBlank;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Tài khoản mật khẩu đăng nhập", description = "Đối tượng gửi lên để đăng nhập")
public class LoginRequest {

    @NullOrNotBlank(message = "Hãy nhập tên tài khoản của bạn!")
    @ApiModelProperty(value = "Tên tài khoản", allowableValues = "NonEmpty String")
    private String username;

    @NullOrNotBlank(message = "Hãy nhập mật khẩu để đăng nhập")
    @ApiModelProperty(value = "Mật khẩu", required = true, allowableValues = "NonEmpty String")
    private String password;

    @Valid
    @NotNull(message = "Thông tin thiết bị không được để trống")
    @ApiModelProperty(value = "THông tin thiết bị", required = true, dataType = "object", allowableValues = "A valid " +
            "deviceInfo object")
    private DeviceInfo deviceInfo;

}
