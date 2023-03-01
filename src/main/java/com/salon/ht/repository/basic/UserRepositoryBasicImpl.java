package com.salon.ht.repository.basic;

import com.salon.ht.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepositoryBasicImpl extends EntityRepository implements UserRepositoryBasic {
    @Override
    public Page<UserEntity> getUsers(String name, Integer status, String mobile, String email, Long departmentId,
                                     String fromDate, String toDate, PageRequest pageRequest) {
        String sqlWhere = "";

        Map<String, Object> params = new HashMap<>();
        if (name != null && !"".equalsIgnoreCase(name)) {
            sqlWhere += " AND ( u.username LIKE :name OR u.name LIKE :name)";
            params.put("name", "%" + name + "%");
        }

        if (email != null) {
            sqlWhere += " AND u.email = :email";
            params.put("email", email);
        }

        if (status != null) {
            sqlWhere += " AND u.status=:status";
            params.put("status", status);
        }

        if (mobile != null && !mobile.equals("")) {
            sqlWhere += " AND u.mobile=:mobile";
            params.put("mobile", mobile);
        }

        if (fromDate != null && !"".equalsIgnoreCase(fromDate) && toDate != null && !"".equalsIgnoreCase(toDate)) {
            sqlWhere += " AND u.created_date between :fromDate AND :toDate";
            params.put("fromDate", fromDate);
            params.put("toDate", toDate);
        }
        String sqlQuery = "SELECT * FROM user u WHERE 1=1";
        String sqlCountQuery = "SELECT count(*) FROM user u WHERE 1=1";

        if (departmentId != null) {
            sqlQuery = "SELECT u.* FROM user u INNER JOIN users_departments ud ON u.id = ud.user_id " +
                    "INNER JOIN department d ON d.id = ud.department_id WHERE 1=1";
            sqlCountQuery = "SELECT count(*) FROM user u INNER JOIN users_departments ud ON u.id = ud.user_id " +
                    "INNER JOIN department d ON d.id = ud.department_id WHERE 1=1";
            sqlWhere += " AND d.id = :departmentId";
            params.put("departmentId", departmentId);
        }

        sqlQuery += sqlWhere;
        sqlCountQuery += sqlWhere;

        return fetchPaging(sqlQuery, sqlCountQuery, params, UserEntity.class, pageRequest);
    }
}
