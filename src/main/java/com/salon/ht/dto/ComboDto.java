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

    private Long pkId;

    public ComboDto(Long id, String name, Long price, Integer status, String image, Integer orderBy, Long pkId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.status = status;
        this.image = image;
        this.orderBy = orderBy;
        this.pkId = pkId;
    }
}
