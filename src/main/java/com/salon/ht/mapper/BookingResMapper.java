package com.salon.ht.mapper;

import com.salon.ht.config.Constant;
import com.salon.ht.entity.Booking;
import com.salon.ht.entity.payload.BookingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.time.LocalDateTime;


@Mapper(componentModel = "spring")
public interface BookingResMapper {

    @Mappings({
            @Mapping(source = "createdDate", target = "createdDate", qualifiedByName = "mapLocalDateTimeToString"),
            @Mapping(source = "modifiedDate", target = "modifiedDate", qualifiedByName = "mapLocalDateTimeToString"),
            @Mapping(source = "startTime", target = "startTime", qualifiedByName = "mapLocalDateTimeToString"),
            @Mapping(source = "endTime", target = "endTime", qualifiedByName = "mapLocalDateTimeToString")
    })
    BookingResponse toDto(Booking entity);

    @Named("mapLocalDateTimeToString")
    default String mapLocalDateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(Constant.DATE_TIME_FORMATTER);
    }
}
