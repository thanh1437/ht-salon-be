package com.salon.ht.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "USERS_DEPARTMENTS")
@Table(name = "users_departments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDepartment extends AbstractModel<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    @Column(name = "created_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Date createdDate;
    @Column(name = "modified_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date modifiedDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    @Column(name = "position")
    private String position;
    @Column(name = "job_title")
    private String jobTile;
    @Column(name = "is_root", columnDefinition = "tinyint(4) DEFAULT 0")
    private Boolean isRoot;
    @Column(name = "is_admin", columnDefinition = "tinyint(4) DEFAULT 0")
    private Boolean isAdmin;
}
