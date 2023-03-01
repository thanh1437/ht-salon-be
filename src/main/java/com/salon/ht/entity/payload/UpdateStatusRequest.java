package com.salon.ht.entity.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "Cập nhật Trạng thái", description = "Playload dùng để cập nhật trạng thái")
public class UpdateStatusRequest {

    @ApiModelProperty(value = "Trạng thái", dataType = "Integer", required = true)
    @NotNull(message = "Trạng thái không được trống")
    private Integer status;
}
