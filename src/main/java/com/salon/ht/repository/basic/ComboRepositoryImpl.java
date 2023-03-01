package com.salon.ht.repository.basic;

import com.salon.ht.entity.Booking;
import com.salon.ht.entity.Combo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ComboRepositoryImpl extends EntityRepository implements ComboRepositoryBasic {
    @Override
    public Page<Combo> getCombos(String name, String code, String fromDate, String toDate, Integer status, PageRequest pageRequest) {
        String sqlWhere = "";
        Map<String, Object> params = new HashMap<>();
        if (name != null) {
            sqlWhere += " AND b.name LIKE :name";
            params.put("name", "%" + name + "%");
        }

        if (code != null) {
            sqlWhere += " AND b.code = :code";
            params.put("code", code);
        }

        if (fromDate != null && !"".equalsIgnoreCase(fromDate) && toDate != null && !"".equalsIgnoreCase(toDate)) {
            sqlWhere += " AND b.created_date between STR_TO_DATE(:fromDate, '%d/%m/%Y') AND STR_TO_DATE(:toDate, '%d/%m/%Y') ";
            params.put("fromDate", fromDate);
            params.put("toDate", toDate);
        }

        if (status != null) {
            sqlWhere += " AND b.status= :status";
            params.put("status", status);
        }

        String sqlQuery = "SELECT * FROM combo b WHERE 1=1 " + sqlWhere;

        String sqlCountQuery = "SELECT count(*) FROM combo b where 1=1" + sqlWhere;
        LOGGER.info("Query {}", sqlQuery);
        return fetchPaging(sqlQuery, sqlCountQuery, params, Combo.class, pageRequest);
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(ComboRepositoryImpl.class);
}
