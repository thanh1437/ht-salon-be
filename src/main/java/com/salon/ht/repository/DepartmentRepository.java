package com.salon.ht.repository;

import com.salon.ht.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query(value = "SELECT * FROM department WHERE (:name is null or concat(`name`,`short_name`) like %:name%) and status = :status",
            countQuery = "SELECT COUNT(*) FROM department WHERE (:name is null or concat(`name`,`short_name`) like %:name%) and status = :status",
            nativeQuery = true)
    List<Department> getList(@Param("name") String name, @Param("status") boolean status);

    Boolean existsByName(String name);

    Optional<Department> findByDepartmentPath(String departmentPath);

    @Query(value = "SELECT d FROM Department d WHERE d.parentId = :id")
    List<Department> isParentDepartment(@Param("id") Long id);
}
