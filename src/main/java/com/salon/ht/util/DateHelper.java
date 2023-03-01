package com.salon.ht.util;

import com.salon.ht.config.Constant;
import org.mapstruct.Named;

import java.time.LocalDateTime;

public class DateHelper {
    @Named("localDateTimeToString")
    public static String localDateTimeToString(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(Constant.DATE_FORMAT);
    }
}
