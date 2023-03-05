package com.salon.ht.repository.basic;

import com.salon.ht.entity.Booking;
import com.salon.ht.entity.Combo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface ComboRepositoryBasic {
    Page<Combo> getCombos(@Param("name") String name,
                          @Param("code") String code,
                          PageRequest pageRequest);
}
