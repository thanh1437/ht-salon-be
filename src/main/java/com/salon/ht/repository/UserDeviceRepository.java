package com.salon.ht.repository;

import com.salon.ht.entity.RefreshToken;
import com.salon.ht.entity.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {

    Optional<UserDevice> findByRefreshToken(RefreshToken refreshToken);

    Optional<UserDevice> findByUserId(Long userId);

    Optional<UserDevice> findByUserIdAndDeviceId(Long userId, String deviceId);
}
