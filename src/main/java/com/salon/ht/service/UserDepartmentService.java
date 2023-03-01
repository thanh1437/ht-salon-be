package com.salon.ht.service;

import com.salon.ht.repository.UserDepartmentRepository;
import com.salon.ht.entity.UserDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class UserDepartmentService extends AbstractService<UserDepartment, Long> {
    private final UserDepartmentRepository userDepartmentRepository;

    public UserDepartmentService(UserDepartmentRepository userDepartmentRepository) {
        this.userDepartmentRepository = userDepartmentRepository;
    }

    @Override
    protected JpaRepository<UserDepartment, Long> getRepository() {
        return userDepartmentRepository;
    }
}
