package com.salon.ht.mapper;

import com.salon.ht.entity.Booking;
import com.salon.ht.entity.payload.BookingResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-01T14:58:38+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.14 (Oracle Corporation)"
)
@Component
public class BookingResMapperImpl implements BookingResMapper {

    @Override
    public BookingResponse toDto(Booking entity) {
        if ( entity == null ) {
            return null;
        }

        BookingResponse bookingResponse = new BookingResponse();

        bookingResponse.setCreatedDate( mapLocalDateTimeToString( entity.getCreatedDate() ) );
        bookingResponse.setModifiedDate( mapLocalDateTimeToString( entity.getModifiedDate() ) );
        bookingResponse.setStartTime( mapLocalDateTimeToString( entity.getStartTime() ) );
        bookingResponse.setEndTime( mapLocalDateTimeToString( entity.getEndTime() ) );
        bookingResponse.setId( entity.getId() );
        bookingResponse.setCode( entity.getCode() );
        bookingResponse.setTitle( entity.getTitle() );
        bookingResponse.setChooseUserId( entity.getChooseUserId() );
        bookingResponse.setDescription( entity.getDescription() );
        bookingResponse.setTakePhoto( entity.getTakePhoto() );
        bookingResponse.setBookingStatus( entity.getBookingStatus() );
        bookingResponse.setCreateBy( entity.getCreateBy() );
        bookingResponse.setModifiedBy( entity.getModifiedBy() );

        return bookingResponse;
    }
}
