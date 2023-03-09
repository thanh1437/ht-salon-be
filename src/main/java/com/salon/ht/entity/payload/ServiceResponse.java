package com.salon.ht.entity.payload;

import lombok.Data;

import java.io.Serializable;

@Data
public class ServiceResponse implements Serializable {

    private Long id;

    private String name;

    private String description;

    private String code;

    private Integer orderBy;

    private Long price;

    private Long duration;

    private Integer status;

    private String createBy;

    private String modifiedBy;

    private String createdDate;

    private String modifiedDate;

    private String image;

}
