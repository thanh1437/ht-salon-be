package com.salon.ht.repository;

import com.salon.ht.entity.Booking;
import com.salon.ht.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Long>{

    List<Service> findByIdIn(List<Long> serviceIds);

    Optional<Service> findTopByOrderByIdDesc();

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE service c set c.status = :status WHERE c.id IN :ids", nativeQuery = true)
    @Transactional
    void updateStatus(List<Long> ids, Integer status);
}
