package com.salon.ht.entity;

import com.salon.ht.constant.RoleName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "role", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Role extends AbstractModel<Long> {
    
    @Column(length = 20)
    private String name;
    @Column(name = "detail")
    private String detail;
    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<UserEntity> users;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id")
    )
    private Set<Permission> permissions;

    public boolean isAdminRole() {
        return this.name.equals(RoleName.ROLE_ADMIN.name());
    }
}
