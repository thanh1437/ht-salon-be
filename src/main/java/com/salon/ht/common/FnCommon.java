package com.salon.ht.common;

import com.salon.ht.exception.BadRequestException;

import java.time.LocalDateTime;

import static com.salon.ht.config.Constant.DATE_FORMAT;

public class FnCommon {

    public static LocalDateTime convertStringToLDT(String localDateTime) {
        try {
           return LocalDateTime.parse(localDateTime, DATE_FORMAT) ;
        } catch (Exception e) {
            throw new BadRequestException("Thời gian sai định dạng");
        }
    }

}
