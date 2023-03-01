package com.salon.ht.repository.basic;

import com.salon.ht.entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public abstract class EntityRepository {

    @PersistenceContext
    private EntityManager entityManager;

    protected <T> Page<T> fetchPaging(String sqlQuery,
                                      String sqlCountQuery, Map<String, Object> params,
                                      Class<T> clazz, PageRequest pageRequest) {
        Query query = entityManager.createNativeQuery(sqlQuery, clazz);
        query.setMaxResults(pageRequest.getPageSize());
        query.setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize());
        Query countQuery = entityManager.createNativeQuery(sqlCountQuery);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
                countQuery.setParameter(entry.getKey(), entry.getValue());
            }
        }

        BigInteger total = (BigInteger) countQuery.getSingleResult();
        return new PageImpl<T>(query.getResultList(), pageRequest, total.longValue());
    }


    protected List<Object[]> fetchList(String sqlQuery, Map<String, Object> params) {
        Query query = entityManager.createNativeQuery(sqlQuery);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        //  System.out.println(sqlQuery);
        return query.getResultList();
    }

    protected Object[] fetchObject(String sqlQuery, Map<String, Object> params) {
        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter(((Map.Entry<String, Object>) params).getKey(),
                ((Map.Entry<String, Object>) params).getValue());
        return (Object[]) query.getSingleResult();
    }

}
