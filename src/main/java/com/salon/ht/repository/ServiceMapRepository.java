package com.salon.ht.repository;

import com.salon.ht.entity.ServiceMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ServiceMapRepository extends JpaRepository<ServiceMap, Long> {

    @Query(value = "SELECT s.service_id FROM service_map s WHERE s.pk_id = :pkId AND s.status = 1", nativeQuery = true)
    List<Long> findServiceIdsByPkId(Long pkId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE service_map sl set sl.status= :status WHERE sl.pk_id= :pkId AND sl.table_name IN :tableNames", nativeQuery = true)
    @Transactional
    void updateStatusByPkId(Long pkId, List<String> tableNames, int status);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE service_map sl set sl.status= :status WHERE sl.pk_id IN :pkIds AND sl.table_name = :tableName", nativeQuery = true)
    @Transactional
    void updateStatusByPkIds(List<Long> pkIds, String tableName, int status);

}
