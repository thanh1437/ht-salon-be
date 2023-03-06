package com.salon.ht.repository.basic;

import com.salon.ht.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepositoryBasic {
    Page<Booking> getBooking(@Param("chooseUserId") Long chooseUserId,
                             @Param("name") String name,
                             @Param("fromDate") String fromDate,
                             @Param("toDate") String toDate,
                             @Param("status") Integer status,
                             PageRequest pageRequest);
}
