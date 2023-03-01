package com.salon.ht.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;


@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id", nullable = false)
    private Long id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_device_id", unique = true, referencedColumnName = "user_device_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserDevice userDevice;

    @Column(name = "refresh_count")
    private long refreshCount;

    @Column(name = "expiry_dt", nullable = false)
    private Instant expiryDate;

    @Column(name = "created_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private Date createdDate;
    @Column(name = "modified_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date modifiedDate;

    public void incrementRefreshCount() {
        refreshCount = refreshCount + 1;
    }
}
