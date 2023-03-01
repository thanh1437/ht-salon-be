package com.salon.ht.dto;

import com.salon.ht.constant.BookingStatus;
import com.salon.ht.constant.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto implements Serializable {

    private Long id;

    private String title;

    private Long userId;

    private Long ChooseUserId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String description;

    private Integer takePhoto;

    private String photo;

    private String createBy;

    private String modifiedBy;

    private String createdDate;

    private String modifiedDate;

    @Enumerated(value = EnumType.ORDINAL)
    private BookingStatus bookingStatus;
}
