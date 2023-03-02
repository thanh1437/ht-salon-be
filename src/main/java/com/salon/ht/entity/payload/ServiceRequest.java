package com.salon.ht.entity.payload;

import com.salon.ht.validation.annotation.NullOrNotBlank;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ServiceRequest {

    private Long id;

    @ApiModelProperty(value = "Tên dịch vụ", dataType = "String", required = true)
    @NullOrNotBlank(message = "Hãy chọn tên dịch vụ")
    private String name;

    @ApiModelProperty(value = "Loại dịch vụ", dataType = "Integer", required = true)
    @NotNull(message = "Hãy chọn loại dịch vụ")
    private Integer type;

    @ApiModelProperty(value = "Giá dịch vụ", dataType = "Long", required = true)
    @NotNull(message = "Hãy chọn giá dịch vụ")
    private Long price;

    @ApiModelProperty(value = "Thời gian làm dịch vụ", dataType = "Long", required = true)
    @NotNull(message = "Hãy chọn thời gian làm dịch vụ")
    private Long duration;

    @ApiModelProperty(value = "Trạng thái dịch vụ", dataType = "Long", required = true)
    @NotNull(message = "Hãy chọn trạng thái dịch vụ")
    private Integer status;

    @ApiModelProperty(value = "Ảnh dịch vụ", dataType = "String", required = true)
    @NullOrNotBlank(message = "Hãy chọn ảnh dịch vụ")
    private String image;

}
