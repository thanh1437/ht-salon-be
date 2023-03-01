package com.salon.ht.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "service_map")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ServiceMap extends AbstractModel<Long> {

    @Column(name = "pk_id", updatable = false)
    private Long pkId;

    @Column(name = "service_id", updatable = false)
    private Long serviceId;

    @Column(name = "user_id", updatable = false)
    private Long userId;

    @Column(name = "table_name", updatable = false)
    private String tableName;

    @Column(name = "status")
    private Integer status;

}
