package com.salon.ht.entity.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UpdateStatusListRequest {

    @ApiModelProperty(value = "Dánh sách mã yêu cầu cập nhật", required = true)
    @NotNull(message = "Dánh sách mã yêu cầu không được bỏ trống")
    List<Long> ids;

    @ApiModelProperty(value = "Trạng thái cần cập nhật", dataType = "Integer", required = true)
    @NotNull(message = "Hãy nhập trạng thái cần cập nhật")
    Integer status;

    String photo;
}
