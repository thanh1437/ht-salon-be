package com.salon.ht.controller;

import com.salon.ht.constant.RedisKey;
import com.salon.ht.entity.RefreshToken;
import com.salon.ht.entity.payload.ApiResponse;
import com.salon.ht.entity.payload.JwtAuthenticationResponse;
import com.salon.ht.entity.payload.LoginRequest;
import com.salon.ht.entity.payload.LogoutRequest;
import com.salon.ht.entity.payload.RegistrationRequest;
import com.salon.ht.entity.payload.TokenRefreshRequest;
import com.salon.ht.exception.TokenRefreshException;
import com.salon.ht.exception.UserLoginException;
import com.salon.ht.exception.UserRegistrationException;
import com.salon.ht.security.jwt.JwtTokenProvider;
import com.salon.ht.security.service.UserDetailsImpl;
import com.salon.ht.util.AppUtils;
import com.salon.ht.util.RedisUtils;
import com.salon.ht.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Api(tags = "AuthService", description = "Định nghĩa các api liên quan đến việc xác thực người dùng, đăng xuất người dùng")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider tokenProvider;


    @Value("${app.jwt.expiration}")
    private long accessTokenExpired;

    /**
     * Entry point for the user log in. Return the jwt auth token and the refresh token
     */
    @PostMapping("/login")
    @ApiOperation(value = "Đăng nhập người dùng vào hệ thống và trả về token")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authService.authentication(loginRequest)
                .orElseThrow(() -> new UserLoginException("Không thể đăng nhập với tài khoản: [" + loginRequest + "]"));

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        userDetails.setPhoto(null);

        LOGGER.info("Logged user return [API]: " + userDetails.getUsername());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authService.createAndPersistRefreshTokenForDevice(authentication, loginRequest)
                .map(RefreshToken::getToken)
                .map(refreshToken -> {
                    String jwtToken = authService.generateToken(userDetails);
                    // save token to cache
                    String accessTokenHash = AppUtils.hash(jwtToken);
                    String redisKey = RedisKey.ACCESS_TOKENS_PREFIX + userDetails.getId() + "_" + accessTokenHash;
                    RedisUtils.set(redisKey, "1", accessTokenExpired);
                    return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken, refreshToken, tokenProvider.getExpiration()));
                }).orElseThrow(() -> new UserLoginException("Xảy ra lỗi khi khở tạo token cho user: [" + loginRequest + "]"));
    }

    /**
     * Entry point for the user registration process. On successful registration,
     * publish an event to generate email verification token
     */
    @PostMapping("/register")
    @ApiOperation(value = "Đăng ký người dùng mới")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) {
        return authService.registerUser(request)
                .map(user -> ResponseEntity.ok(new ApiResponse(true, "Đăng ký người dùng thành công")))
                .orElseThrow(() -> new UserRegistrationException(request.getUsername(), "Đăng ký người dùng thất bại"));
    }

    /**
     * Refresh the expired jwt token using a refresh token for the specific device
     * and return a new token to the caller
     */
    @PostMapping("/refresh")
    @ApiOperation(value = "Làm mới lại access token với refresh token")
    public ResponseEntity<?> refreshJwtToken(@Valid TokenRefreshRequest tokenRefreshRequest) {
        return authService.refreshJwtToken(tokenRefreshRequest)
                .map(updateToken -> {
                    String refreshToken = tokenRefreshRequest.getRefreshToken();
                    LOGGER.info("Created new Jwt Authentication token: " + updateToken);
                    return ResponseEntity.ok(new JwtAuthenticationResponse(updateToken, refreshToken, tokenProvider.getExpiration()));
                })
                .orElseThrow(() -> new TokenRefreshException(tokenRefreshRequest.getRefreshToken(), "Có lỗi xảy ra khi thực hiện refresh token. Vui lòng đăng nhập lại và thử lại"));
    }

    /**
     * Log the user out from the app/device. Release the refresh token associated with the
     * user device.
     */
    @PostMapping("/logout")
    @ApiOperation(value = "Đăng xuất người dùng khỏi hệ thống và xóa toàn bộ phiên đăng nhập của người dùng")
    public ResponseEntity<?> logoutUser(@ApiParam(value = "LogOutRequest payload") @Valid @RequestBody LogoutRequest logOutRequest) {
        authService.logoutUser(logOutRequest);
        return ResponseEntity.ok(new ApiResponse(true, "Đăng xuất thành công !"));
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
}
