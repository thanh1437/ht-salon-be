package com.salon.ht.entity.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailResp {
    private Long id;

    private String code;

    private String userName;

    private String email;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
