package com.salon.ht.common;

import com.salon.ht.exception.BadRequestException;

import java.time.LocalDateTime;

import static com.salon.ht.config.Constant.DATE_TIME_FORMATTER;

public class FnCommon {

    public static LocalDateTime convertStringToLDT(String localDateTime) {
        try {
           return LocalDateTime.parse(localDateTime, DATE_TIME_FORMATTER) ;
        } catch (Exception e) {
            throw new BadRequestException("Thời gian sai định dạng");
        }
    }

}
