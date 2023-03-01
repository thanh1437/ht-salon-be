package com.salon.ht.entity.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "Tạo mới yêu cầu", description = "Payload dùng để tạo mới yêu cầu")
public class BookingRequest implements Serializable {

    @ApiModelProperty(value = "Chọn người làm", dataType = "Long")
    private Long id;

    @ApiModelProperty(value = "Chọn người làm", dataType = "Long", required = true)
    @NotNull(message = "Hãy chọn người cắt")
    private Long chooseUserId;

    @ApiModelProperty(value = "Thời gian bắt đầu", dataType = "Date", required = true)
    @NotNull(message = "Hãy nhập thời gian bắt đầu")
    private String startTime;


    @ApiModelProperty(value = "Yêu cầu thêm (nếu có)", dataType = "String")
    private String description;

    @ApiModelProperty(value = "Chụp lại ảnh", dataType = "Integer", required = true)
    @NotNull(message = "Hãy chọn có chụp ảnh không")
    private Integer takePhoto;

    @ApiModelProperty(value = "Danh sách dịch vụ")
    private List<Long> serviceIds;

    @ApiModelProperty(value = "Danh sách combo")
    private List<Long> comboIds;

    private boolean isCombo;

}
