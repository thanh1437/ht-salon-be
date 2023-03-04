package com.salon.ht.entity.payload;

import com.salon.ht.validation.annotation.NullOrNotBlank;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Yêu cầu đăng ký", description = "Payload dùng để đăng ký mới")
public class RegistrationRequest {

    @NullOrNotBlank(message = "Username can't null or blank")
    @ApiModelProperty(value = "A valid username", allowableValues = "NonEmpty String")
    private String username;

    @NullOrNotBlank(message = "Email can't null or blank")
    @ApiModelProperty(value = "A valid email", required = true, allowableValues = "NonEmpty String")
    private String email;

    @NotNull(message = "Password can't null or blank")
    @ApiModelProperty(value = "A valid password string", required = true, allowableValues = "NonEmpty String")
    private String password;

    @NotNull(message = "Specify whether the user has to be registered as an admin or not")
    @ApiModelProperty(value = "Flag denoting whether the user is an admin or not", required = true,
            dataType = "boolean", allowableValues = "true, false")
    private String role;

    @NotNull(message = "Mobile can't null")
    @ApiModelProperty(value = "A valid mobile string", required = true, allowableValues = "NonEmpty String")
    private String mobile;

    @NotNull(message = "Name can't null")
    @ApiModelProperty(value = "A valid name string", required = true, allowableValues = "NonEmpty String")
    private String name;
}
