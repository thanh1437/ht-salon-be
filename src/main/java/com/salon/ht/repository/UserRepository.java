package com.salon.ht.repository;

import com.salon.ht.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(rollbackOn = Exception.class)
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query(value = "SELECT u.* FROM user u WHERE (:name is null or u.name like %:username%) " +
            "and (:email is null or u.email = :email)",
            countQuery = "SELECT COUNT(*) FROM user WHERE (:name is null or name like %:username%) " +
                    "and (:email is null or email = :email)",
            nativeQuery = true)
    Page<UserEntity> getList(String name, String email, Pageable pageable);

    @Query(value = "SELECT u FROM UserEntity u WHERE u.id in :userIds")
    List<UserEntity> getByIds(@Param("userIds") List<Long> userIds);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE user SET status=:status WHERE id=:userId", nativeQuery = true)
    void updateStatus(@Param("userId") Long userId, @Param("status") Integer status);

    @Modifying
    @Query("delete from UserEntity u where u.id=:id")
    @Transactional
    void deleteById(@Param("id") Long id);

    @Query(value = "SELECT u FROM UserEntity u LEFT JOIN Booking b ON u.id = b.userId WHERE b.userId in :bookingIds")
    List<UserEntity> getByBookingIds(List<Long> bookingIds);

    Optional<UserEntity> findTopByOrderByIdDesc();
}