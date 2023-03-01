package com.salon.ht.entity.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel(value = "Yêu cầu đăng xuất", description = "Payload yêu cầu đăng xuất")
@Data
@Setter
@Getter
public class LogoutRequest {

    @Valid
    @NotNull(message = "Thông tin thiết bị đăng xuất không được trống")
    @ApiModelProperty(value = "Thông tin thiết bị", required = true, dataType = "object", allowableValues = "A valid " +
            "deviceInfo object")
    private DeviceInfo deviceInfo;

    @NotEmpty(message = "Access token sử dụng để logout không được trống")
    private String token;
}
