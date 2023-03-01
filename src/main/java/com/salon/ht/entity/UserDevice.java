package com.salon.ht.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.salon.ht.constant.DeviceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "USER_DEVICE")
@Table(name = "user_device")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_device_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "device_type")
    @Enumerated(value = EnumType.STRING)
    private DeviceType deviceType;

    @Column(name = "notification_token")
    private String notificationToken;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @OneToOne(optional = false, mappedBy = "userDevice", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RefreshToken refreshToken;

    @Column(name = "is_refresh_active")
    boolean isRefreshActive;
    @Column(name = "created_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Date createdDate;
    @Column(name = "modified_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date modifiedDate;

}
