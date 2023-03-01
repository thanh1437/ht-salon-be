package com.salon.ht.entity.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Yêu cầu tạo mới token", description = "Payload yêu cầu tạo mới token")
public class TokenRefreshRequest {
    
    @NotBlank(message = "Tạo mới token không được trống")
    @ApiModelProperty(value = "Valid refresh token passed during earlier successful authentications", required = true,
            allowableValues = "NonEmpty String")
    private String refreshToken;
}
