package com.salon.ht.entity.payload;

import com.salon.ht.validation.annotation.NullOrNotBlank;
import com.salon.ht.dto.DeptUserDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "Tạo mới, cập nhật phòng ban", description = "Playload dùng để tạo mới hoặc cập nhật phòng ban")
public class DepartmentRequest implements Serializable {
    @ApiModelProperty(value = "Id phòng ban", dataType = "Long", required = true)
    @NotNull(message = "Id phòng ban không được trống")
    private Long id;

    @ApiModelProperty(value = "Tên rút gọn của phòng ban", dataType = "String", required = true)
    @NullOrNotBlank(message = "Tên rút gọn của phòng ban có thể null nhưng không được để trống")
    private String shortName;

    @ApiModelProperty(value = "Tên phòng ban", dataType = "String", required = true)
    @NotNull(message = "Tên phòng ban không được để trống")
    private String name;

    @ApiModelProperty(value = "Trạng thái của phòng ban", dataType = "Boolean", required = true)
    @NotNull(message = "Hãy chọn trạng thái của phòng ban")
    private Boolean status;

    @ApiModelProperty(value = "ID phòng ban cha của phòng ban hiện tại", required = true)
    @NotNull(message = "ID phòng ban cha của phòng ban hiện tại không được trống")
    private Long parentId;

    @ApiModelProperty(value = "Người dùng thuộc phòng ban hiện tại", dataType = "", required = true)
    @NotNull(message = "Phải có tham dố danh sách nhân viên thuộc phòng ban. Danh sách có thể rỗng")
    private List<DeptUserDto> users;
}
