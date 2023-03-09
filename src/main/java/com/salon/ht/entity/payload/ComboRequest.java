package com.salon.ht.entity.payload;

import com.salon.ht.validation.annotation.NullOrNotBlank;
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

    @ApiModelProperty(value = "Mô tả", dataType = "String", required = true)
    @NullOrNotBlank(message = "Hãy chọn thêm mô tả")
    private String description;

    @ApiModelProperty(value = "Giá combo", dataType = "Long", required = true)
    @NotNull(message = "Hãy chọn giá")
    private Long price;

    @ApiModelProperty(value = "Trạng thái", dataType = "Integer", required = true)
    @NotNull(message = "Hãy chọn trạng thái")
    private Integer status;

    @ApiModelProperty(value = "Các dịch vụ", required = true)
    @NotNull(message = "Hãy chọn dịch vụ")
    private List<Long> serviceIds;

    @ApiModelProperty(value = "Ảnh", dataType = "String", required = true)
    @NotNull(message = "Hãy chọn ảnh")
    private String image;

    @ApiModelProperty(value = "Độ ưu tiên", dataType = "Integer", required = true)
    @NotNull(message = "Hãy chọn độ ưu tiên")
    private Integer orderBy;

}
