package com.salon.ht.repository.basic;

import com.salon.ht.config.Constant;
import com.salon.ht.entity.Booking;
import com.salon.ht.entity.payload.EmailResp;
import com.salon.ht.security.service.UserDetailsImpl;
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
public class BookingRepositoryImpl extends EntityRepository implements BookingRepositoryBasic {

    @PersistenceContext
    private EntityManager entityManager;

    public List<EmailResp> getEmailRespByBookingIds(List<Long> bookingIds) {
        String jpql = "SELECT NEW com.salon.ht.entity.payload.EmailResp(b.id, b.code, u.username, u.email, b.startTime, b.endTime) " +
                "FROM Booking b LEFT JOIN UserEntity u ON b.userId = u.id WHERE b.id IN :bookingIds ";
        TypedQuery<EmailResp> query = entityManager.createQuery(jpql, EmailResp.class);
        query.setParameter("bookingIds", bookingIds);
        return query.getResultList();
    }

    @Override
    public Page<Booking> getBooking(UserDetailsImpl userDetails, Long chooseUserId, String name,
                                    String fromDate, String toDate, Integer status, PageRequest pageRequest) {

        boolean isAdmin = userDetails.getRoles().stream().anyMatch(role -> Constant.ROLES.ROLE_ADMIN.name().equals(role.getName()));

        String sqlWhere = "";
        Map<String, Object> params = new HashMap<>();

        if (!isAdmin) {
            sqlWhere += " AND (b.choose_user_id = :usId OR b.user_id = :usId)";
            params.put("usId", userDetails.getId());
        }

        if (chooseUserId != null) {
            sqlWhere += " AND b.choose_user_id = :chooseUserId";
            params.put("chooseUserId", chooseUserId);
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

        if (name != null) {
            sqlWhere += " AND (LOWER(b.create_by) LIKE LOWER(:name) OR LOWER(b.code) LIKE LOWER(:name)) ";
            params.put("name", "%" + name + "%");
        }

        String sqlQuery = "SELECT * FROM booking b WHERE 1=1 " + sqlWhere;

        String sqlCountQuery = "SELECT count(*) FROM booking b where 1=1" + sqlWhere;
        LOGGER.info("Query {}", sqlQuery);
        return fetchPaging(sqlQuery, sqlCountQuery, params, Booking.class, pageRequest);
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(BookingRepositoryImpl.class);
}
