package com.salon.ht.service;

import com.salon.ht.exception.TokenRefreshException;
import com.salon.ht.repository.UserDeviceRepository;
import com.salon.ht.entity.RefreshToken;
import com.salon.ht.entity.UserDevice;
import com.salon.ht.entity.payload.DeviceInfo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDeviceService {

    private final UserDeviceRepository userDeviceRepository;

    public UserDeviceService(UserDeviceRepository userDeviceRepository) {
        this.userDeviceRepository = userDeviceRepository;
    }

    /**
     * Find by user id
     */
    public Optional<UserDevice> findByUserId(Long userId) {
        return userDeviceRepository.findByUserId(userId);
    }

    public Optional<UserDevice> findByUserIdDeviceId(Long userId, String deviceId) {
        return userDeviceRepository.findByUserIdAndDeviceId(userId, deviceId);
    }

    /**
     * Find by Refresh Token
     */
    public Optional<UserDevice> findByRefreshToken(RefreshToken refreshToken) {
        return userDeviceRepository.findByRefreshToken(refreshToken);
    }

    /**
     * Creates a new user device and set the user to the current device
     */
    public UserDevice createUserDevice(DeviceInfo deviceInfo) {
        UserDevice userDevice = new UserDevice();
        userDevice.setDeviceId(deviceInfo.getDeviceId());
        userDevice.setNotificationToken(deviceInfo.getNotificationToken());
        userDevice.setDeviceType(deviceInfo.getDeviceType());
        userDevice.setRefreshActive(true);
        return userDevice;
    }

    /**
     * Check whether the user device corresponding to the token has refresh enabled and
     * throw appropriate errors to the client
     */
    void verifyRefreshAvailability(RefreshToken refreshToken) {
        UserDevice userDevice = findByRefreshToken(refreshToken)
                .orElseThrow(() -> new TokenRefreshException(refreshToken.getToken(), "No device found for the matching token. Please login again"));

        if (!userDevice.isRefreshActive()) {
            throw new TokenRefreshException(refreshToken.getToken(), "Refresh blocked for the device. Please login through a different device");
        }
    }
}
