package com.salon.ht.repository;

import com.salon.ht.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE booking b set b.status = :status WHERE b.id IN :bookingIds", nativeQuery = true)
    @Transactional
    void updateStatus(List<Long> bookingIds, int status);

    @Query(value = "SELECT * FROM booking order by id desc limit 1", nativeQuery = true)
    Optional<Booking> findTopByOrderByIdDesc();

    List<Booking> findByIdIn(List<Long> bookingIds);
}
