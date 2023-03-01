package com.salon.ht.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "department", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Department extends AbstractModel<Long> {

    @Column(name = "name")
    private String name;
    @Column(name = "parent_id")
    private Long parentId;
    @Column(name = "department_ext_id")
    private String departmentExtId;
    @Column(name = "department_path")
    private String departmentPath;
    @Column(name = "short_name")
    private String shortName;
    @Column(name = "status")
    private boolean status;
    @Column(name = "created_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Date createdDate;
    @Column(name = "modified_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date modifiedDate;
    @OneToMany(mappedBy = "department", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<UserDepartment> departmentUsers;
}
