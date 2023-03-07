package com.salon.ht.entity.payload;

import com.salon.ht.constant.BookingStatus;
import com.salon.ht.dto.ComboDto;
import com.salon.ht.dto.ServiceDto;
import com.salon.ht.dto.UserDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BookingResponse implements Serializable {

    private Long id;

    private String code;

    private String title;

    private String userName;

    private String chooseUser;

    private String startTime;

    private String endTime;

    private String description;

    private Integer takePhoto;

    private Integer status;

    private String createBy;

    private String modifiedBy;

    private String createdDate;

    private String modifiedDate;

    private String photo;

    private Long totalPrice;

    private List<ServiceDto> serviceDtos;

    private List<ComboDto> comboDtos;
}
