package com.salon.ht.repository;

import com.salon.ht.entity.Booking;
import com.salon.ht.entity.Combo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ComboRepository extends JpaRepository<Combo, Long> {

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE combo c set c.status = :status WHERE c.id IN :comboIds", nativeQuery = true)
    @Transactional
    void updateStatus(List<Long> comboIds, Integer status);

    Optional<Combo> findTopByOrderByIdDesc();
}
