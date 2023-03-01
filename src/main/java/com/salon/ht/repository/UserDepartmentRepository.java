package com.salon.ht.repository;

import com.salon.ht.entity.UserDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDepartmentRepository extends JpaRepository<UserDepartment, Long> {
}
