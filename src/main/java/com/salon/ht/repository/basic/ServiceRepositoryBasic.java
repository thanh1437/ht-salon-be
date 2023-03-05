package com.salon.ht.repository.basic;

import com.salon.ht.entity.Booking;
import com.salon.ht.entity.Combo;
import com.salon.ht.entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepositoryBasic {
    Page<Service> getServices(@Param("name") String name,
                              @Param("code") String code,
                              PageRequest pageRequest);
}
