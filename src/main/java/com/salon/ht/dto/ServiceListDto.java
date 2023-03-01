package com.salon.ht.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceListDto implements Serializable {

    private Long id;

    private Long bookingId;

    private Long serviceId;

    private Integer status;

    private String createBy;

    private String modifiedBy;

}
