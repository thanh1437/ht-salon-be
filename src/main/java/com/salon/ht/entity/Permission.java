package com.salon.ht.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "permission")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Permission extends AbstractModel<Long> {

    @Basic
    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "permissions")
    private Collection<Role> roles;
    @Basic
    @Column(name = "mask")
    private String mask;
    @Column(name = "created_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Date createdDate;
    @Column(name = "modified_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date modifiedDate;
}
