package com.salon.ht.entity.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "Tạo mới, cập nhật quyền", description = "Playload dùng để tạo mới hoặc cập nhật quyền")
@Data
public class RoleRequest implements Serializable {
    @ApiModelProperty(value = "Id quyền", dataType = "Long", required = true)
    @NotNull(message = "Id quyền không được trống")
    private Long id;

    @ApiModelProperty(value = "Tên quyền", dataType = "String", required = true)
    @NotNull(message = "Tên quyền không được để trống")
    private String name;

    @ApiModelProperty(value = "Mô tả quyền", dataType = "String", required = true)
    @NotNull(message = "Mô tả quyền không được để trống")
    private String description;

    @ApiModelProperty(value = "Chi tiết quyền", dataType = "String", required = true)
    @NotNull(message = "Chi tiết quyền không được để trống")
    private String detail;

    @ApiModelProperty(value = "Có phải quyền quản trị không", dataType = "Boolean", required = true)
    @NotNull(message = "Hãy cấu hình quyền quản trị hay không")
    private Boolean isAdminRole;

    @ApiModelProperty(value = "Danh sách ID người dùng có quyền đó", dataType = "", required = true)
    @NotEmpty(message = "Danh sách ID người dùng có quyền đó có thể null. Nhưng không thể thiếu")
    private List<Long> userIds;
}
