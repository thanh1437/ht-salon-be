package com.salon.ht.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ServiceDto implements Serializable {

    private Long id;

    private String code;

    private String name;

    private Integer type;

    private Long price;

    private Long duration;

    private Integer status;

    private String createBy;

    private String modifiedBy;

    private String createdDate;

    private String modifiedDate;

    private Long pkId;

    public ServiceDto(Long id, String name, String code, Integer type, Long price, Long duration,
                      Integer status, Long pkId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.type = type;
        this.price = price;
        this.duration = duration;
        this.status = status;
        this.pkId = pkId;
    }
}
