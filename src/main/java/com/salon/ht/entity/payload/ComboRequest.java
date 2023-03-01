package com.salon.ht.entity.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class ComboRequest {

    private Long id;

    @ApiModelProperty(value = "Tên combo", dataType = "String", required = true)
    @NotNull(message = "Hãy chọn tên")
    private String name;

    @ApiModelProperty(value = "Giá combo", dataType = "Long", required = true)
    @NotNull(message = "Hãy chọn giá")
    private Long price;

    @ApiModelProperty(value = "Trạng thái", dataType = "Integer", required = true)
    @NotNull(message = "Hãy chọn trạng thái")
    private Integer status;

    @ApiModelProperty(value = "Các dịch vụ", required = true)
    @NotNull(message = "Hãy chọn dịch vụ")
    private List<Long> serviceIds;

}
