package com.salon.ht.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.salon.ht.constant.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserEntity extends AbstractModel<Long> {

    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private String code;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "email")
    private String email;
    @Column(name = "created_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Date createdDate;
    @Column(name = "modified_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date modifiedDate;
    @Column(name = "status")
    @Enumerated
    private UserStatus status;
    @Column(name = "photo", columnDefinition = "VARCHAR(255) DEFAULT '/resources/images/default.png'")
    private String photo;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<UserDevice> userDevices;

    public boolean isAdminUser() {
        boolean result = false;
        if (roles != null && this.roles.size() > 0) {
            result = roles.stream().anyMatch(Role::isAdminRole);
        }
        return result;
    }

    public UserEntity(UserEntity userEntity) {
        setId(userEntity.getId());
        setUsername(userEntity.getUsername());
        setCode(userEntity.getCode());
        setPassword(userEntity.getPassword());
        setName(userEntity.getName());
        setEmail(userEntity.getEmail());
        setMobile(userEntity.getMobile());
        setStatus(userEntity.getStatus());
        setRoles(userEntity.getRoles());
        setCreatedDate(userEntity.getCreatedDate());
        setModifiedDate(userEntity.getModifiedDate());
        setPhoto(userEntity.getPhoto());
    }

    public UserEntity getUserEntity() {
        return this;
    }
}
