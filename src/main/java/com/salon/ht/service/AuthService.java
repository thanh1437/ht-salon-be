package com.salon.ht.service;

import com.salon.ht.constant.RedisKey;
import com.salon.ht.entity.RefreshToken;
import com.salon.ht.entity.UserDevice;
import com.salon.ht.entity.UserEntity;
import com.salon.ht.entity.payload.LoginRequest;
import com.salon.ht.entity.payload.LogoutRequest;
import com.salon.ht.entity.payload.RegistrationRequest;
import com.salon.ht.entity.payload.TokenRefreshRequest;
import com.salon.ht.exception.ResourceAlreadyInUseException;
import com.salon.ht.exception.TokenRefreshException;
import com.salon.ht.exception.UserLogoutException;
import com.salon.ht.security.jwt.JwtTokenProvider;
import com.salon.ht.security.service.UserDetailsImpl;
import com.salon.ht.util.AppUtils;
import com.salon.ht.util.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDeviceService userDeviceService;

    /**
     * Registers a new user in the database by performing a series of quick checks.
     *
     * @return A user object if successfully created
     */
    public Optional<UserEntity> registerUser(RegistrationRequest request) {
        String newRegistrationReqEmail = request.getEmail();
        if (userService.existsByEmail(newRegistrationReqEmail)) {
            LOGGER.error("Email already exists: " + newRegistrationReqEmail);
            throw new ResourceAlreadyInUseException("Email", "Address", newRegistrationReqEmail);
        }

        if (userService.existsByUsername(request.getUsername())) {
            LOGGER.error("Username already exists: " + request.getUsername());
            throw new ResourceAlreadyInUseException("Username", "Name", request.getUsername());
        }

        LOGGER.info("Trying to register new user [" + newRegistrationReqEmail + "]");
        UserEntity newUser = userService.buildUserEntity(request);
        UserEntity registerNewUser = userService.save(newUser);
        return Optional.ofNullable(registerNewUser);
    }

    /**
     * Authenticate user and log them in given a loginRequest
     */
    public Optional<Authentication> authentication(LoginRequest loginRequest) {
        return Optional.ofNullable(authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword())));
    }

    /**
     * Generates a JWT token for the validated client
     */
    public String generateToken(UserDetailsImpl userDetails) {
        return tokenProvider.generateToken(userDetails);
    }


    /**
     * Creates and persists the refresh token for the user device. If device exists
     * already, we don't care. Unused devices with expired tokens should be cleaned
     * with a cron job. The generated token would be encapsulated within the jwt.
     * Remove the existing refresh token as the old one should not remain valid.
     */
    public Optional<RefreshToken> createAndPersistRefreshTokenForDevice(Authentication authentication,
                                                                        LoginRequest loginRequest) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();

        userDeviceService.findByUserIdDeviceId(currentUser.getId(), loginRequest.getDeviceInfo().getDeviceId())
                .map(UserDevice::getRefreshToken)
                .map(RefreshToken::getId)
                .ifPresent(refreshTokenService::deleteById);
        UserDevice userDevice = userDeviceService.createUserDevice(loginRequest.getDeviceInfo());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken();
        userDevice.setUser(currentUser);
        userDevice.setRefreshToken(refreshToken);
        refreshToken.setUserDevice(userDevice);
        RefreshToken newRefreshToken = refreshTokenService.save(refreshToken);
        return Optional.ofNullable(newRefreshToken);
    }

    /**
     * Refresh the expired jwt token using a refresh token and device info. The
     * * refresh token is mapped to a specific device and if it is unexpired, can help
     * * generate a new jwt. If the refresh token is inactive for a device or it is expired,
     * * throw appropriate errors.
     */
    public Optional<String> refreshJwtToken(TokenRefreshRequest refreshRequest) {
        String requestRefreshToken = refreshRequest.getRefreshToken();

        return Optional.of(refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshToken -> {
                    refreshTokenService.verifyExpiration(refreshToken);
                    userDeviceService.verifyRefreshAvailability(refreshToken);
                    refreshTokenService.increaseCount(refreshToken);
                    return refreshToken;
                })
                .map(RefreshToken::getUserDevice)
                .map(UserDevice::getUser)
                .map(UserDetailsImpl::new)
                .map(this::generateToken)
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token truyền lên không tồn tại trong hệ thống")));

    }

    /**
     * Log the given user out and delete the refresh token associated with it. If no device
     * id is found matching the database for the given user, throw a log out exception.
     */
    public void logoutUser(LogoutRequest logOutRequest) {
        String deviceId = logOutRequest.getDeviceInfo().getDeviceId();
        String token = logOutRequest.getToken();
        Long userId = tokenProvider.getUserIdFromToken(token);
        UserDevice userDevice = userDeviceService.findByUserIdDeviceId(userId, deviceId)
                .filter(device -> device.getDeviceId().equals(deviceId))
                .orElseThrow(() -> new UserLogoutException(logOutRequest.getDeviceInfo().getDeviceId(), "Thiết bị truyền lên không hợp lệ. Không có thiết bị nào mà người dùng đăng nhập "));

        LOGGER.info("Removing refresh token associated with device [" + userDevice + "]");
        refreshTokenService.deleteById(userDevice.getRefreshToken().getId());
        // Remove token in cache
        String tokenHash = AppUtils.hash(token);
        String redisKey = RedisKey.ACCESS_TOKENS_PREFIX + userId + "_" + tokenHash;
        RedisUtils.delete(redisKey);
        LOGGER.info("Remove success token in cached with key: {}", redisKey);
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
}
