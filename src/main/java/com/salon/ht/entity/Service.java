package com.salon.ht.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;

@Table(name = "service")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Service extends AbstractModel<Long> {

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "type")  // (dichvu/kieutoc 0/1)
    private Integer type;

    @Column(name = "price")
    private Long price;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "status")
    private Integer status;

    @Column(name = "create_by", updatable = false)
    private String createBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "created_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

}
