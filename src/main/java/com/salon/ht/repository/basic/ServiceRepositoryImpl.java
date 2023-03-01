package com.salon.ht.repository.basic;

import com.salon.ht.dto.ServiceDto;
import com.salon.ht.entity.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ServiceRepositoryImpl extends EntityRepository implements ServiceRepositoryBasic {
    @PersistenceContext
    private EntityManager entityManager;

    public List<ServiceDto> getServiceDtosByPkIdsAndTableName(List<Long> pkId, String tableName) {
        String jpql = "SELECT NEW com.salon.ht.dto.ServiceDto(s.id, s.name, s.type, s.price, s.duration, s.status, sl.pkId) " +
                "FROM ServiceMap sl LEFT JOIN Service s ON sl.serviceId = s.id WHERE sl.pkId IN :pkId AND sl.tableName = :tableName";
        TypedQuery<ServiceDto> query = entityManager.createQuery(jpql, ServiceDto.class);
        query.setParameter("pkId", pkId);
        query.setParameter("tableName", tableName);
        return query.getResultList();
    }


    @Override
    public Page<Service> getServices(String name, String fromDate, String toDate, Integer status, Integer type, String code, PageRequest pageRequest) {
        String sqlWhere = "";
        Map<String, Object> params = new HashMap<>();
        if (name != null) {
            sqlWhere += " AND b.name LIKE :name";
            params.put("name", "%" + name + "%");
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

        if (type != null) {
            sqlWhere += " AND b.type = :type";
            params.put("type", type);
        }

        if (code != null) {
            sqlWhere += " AND b.code = :code";
            params.put("code", code);
        }

        String sqlQuery = "SELECT * FROM service b WHERE 1=1 " + sqlWhere;

        String sqlCountQuery = "SELECT count(*) FROM service b where 1=1" + sqlWhere;
        LOGGER.info("Query {}", sqlQuery);
        return fetchPaging(sqlQuery, sqlCountQuery, params, Service.class, pageRequest);
    }




    private final static Logger LOGGER = LoggerFactory.getLogger(ServiceRepositoryImpl.class);
}