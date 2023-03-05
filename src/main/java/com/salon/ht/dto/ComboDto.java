package com.salon.ht.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ComboDto implements Serializable {

    private Long id;

    private String name;

    private Long price;

    private Integer status;

    private String createBy;

    private String modifiedBy;

    private String createdDate;

    private String modifiedDate;

    private String image;

    private Integer orderBy;
}
