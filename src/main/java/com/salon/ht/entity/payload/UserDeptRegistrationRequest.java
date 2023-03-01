package com.salon.ht.entity.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "Yêu cầu thông tin phòng ban đăng ký", description = "Payload dùng để yêu cầu thông tin phòng ban đăng ký")
public class UserDeptRegistrationRequest implements Serializable {
    @ApiModelProperty(value = "Phòng ban người dùng Id", dataType = "Long", required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "Tên phòng ban", dataType = "String", required = true)
    @NotNull(message = "Tên phòng ban không được để trống")
    private String deptName;

    @ApiModelProperty(value = "Chức vụ của người dùng trong phòng ban đấy", dataType = "String", required = true)
    @NotNull(message = "Chức vụ của người dùng trong phòng ban không được để trống")
    private String position;

    @ApiModelProperty(value = "Chức danh của người dùng trong phòng ban đấy", dataType = "String", required = true)
    @NotNull(message = "Chức danh của người dùng trong phòng ban không được để trống")
    private String jobTitle;

    @ApiModelProperty(value = "Có phải là gốc trong phòng ban không", dataType = "Boolean", required = true)
    @NotNull(message = "Hãy cấu hình có phải gốc trong phòng ban không")
    private Boolean isRoot;

    @ApiModelProperty(value = "Có phải là quản trị trong phòng ban không", dataType = "Boolean", required = true)
    @NotNull(message = "Hãy cấu hình có phải quản trị trong phòng ban không")
    private Boolean isAdmin;
}
