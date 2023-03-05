package com.salon.ht.entity.payload;

import com.salon.ht.dto.ServiceDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ComboResponse implements Serializable {

    private Long id;

    private String name;

    private String code;

    private Long price;

    private String image;

    private Integer status;

    private Integer orderBy;

    private String createBy;

    private String modifiedBy;

    private String createdDate;

    private String modifiedDate;

    private List<ServiceDto> serviceDtos;
}
