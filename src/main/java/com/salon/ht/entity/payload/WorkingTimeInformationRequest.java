package com.salon.ht.entity.payload;

import lombok.Data;

import java.util.List;

@Data
public class WorkingTimeInformationRequest {
    Long userId;
    String date;
    List<Long> serviceIds;
}
