package com.salon.ht.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.salon.ht.exception.InvalidTokenRequestException;
import com.salon.ht.config.RSAKeyProperties;
import com.salon.ht.constant.RedisKey;
import com.salon.ht.security.service.UserDetailsImpl;
import com.salon.ht.util.AppUtils;
import com.salon.ht.util.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt.expiration}")
    private long expiration;
    @Value("${app.jwt.refreshTime}")
    private Integer refreshTime;

    private static final String AUTHORITIES_CLAIM = "authorities";

    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Autowired
    private RSAKeyProperties rsaKeyProperties;

    /**
     * Generate token token
     *
     * @param userDetails user
     * @return token token
     */
    public String generateToken(UserDetailsImpl userDetails) {
        Map<String, Object> claims = this.buildClaims(userDetails);
        return generateToken(userDetails.getUsername(), claims);
    }

    private Map<String, Object> buildClaims(UserDetailsImpl userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String authorities = getUserAuthorities(userDetails);
        claims.put(AUTHORITIES_CLAIM, authorities);
        claims.put("mobile", userDetails.getMobile());
        claims.put("email", userDetails.getEmail());
        claims.put("name", userDetails.getName());
        claims.put("userId", userDetails.getId());
        claims.put("photo", userDetails.getPhoto());
        return claims;
    }

    public boolean verifyToken(String token) {
        Long userId = this.getUserIdFromToken(token);
        if (userId == null) {
            throw new InvalidTokenRequestException("JWT", token, "Token không hợp lệ");
        }
        String accessTokenHash = AppUtils.hash(token);
        boolean keyExist = RedisUtils.hasKey(RedisKey.ACCESS_TOKENS_PREFIX + userId + "_" + accessTokenHash);
        if (!keyExist) {
            throw new InvalidTokenRequestException("JWT", token, "Phiên đăng nhập đã hết, vui lòng đăng nhập lại");
        }
        Date expiration = this.getExpirationToken(token);
        if (!expiration.after(new Date())) {
            throw new InvalidTokenRequestException("JWT", token, "Phiên đăng nhập đã hết, vui lòng đăng nhập lại");
        }
        return true;
    }


    public DecodedJWT decodedJWT(String token) {
        LOGGER.info("Chuan bi verify token " + token);

        try {
            Algorithm algorithm = Algorithm.RSA256(rsaKeyProperties.getPublicKey(), rsaKeyProperties.getPrivateKey());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("booking-meeting")
                    .build(); //Reusable verifier instance
            return verifier.verify(token);
        } catch (Exception e) {
            LOGGER.error("Xảy ra lỗi", e);
            LOGGER.error("Xảy ra lỗi khi verify token: {}", e.getMessage());
        }
        return null;
    }

    public Long getUserIdFromToken(String token) {
        long userId;
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        DecodedJWT decodedJWT = JWT.decode(token);
        Map<String, Claim> claimMap = decodedJWT.getClaims();
        if (claimMap.containsKey("userId")) {
            userId = Long.parseLong(String.valueOf(claimMap.get("userId")));
            return userId;
        } else {
            return null;
        }
    }

    /**
     * Get the user name from the token
     *
     * @param token token
     * @return user name
     */
    public String getUsernameFromToken(String token) {
        String username;
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        DecodedJWT jwt = JWT.decode(token);
        username = jwt.getSubject();
        return username;
    }

    public Date getExpirationToken(String token) {
        Date expiration;
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        DecodedJWT jwt = JWT.decode(token);
        expiration = jwt.getExpiresAt();
        return expiration;
    }

    /**
     * Private helper method to extract user authorities.
     */
    private String getUserAuthorities(UserDetailsImpl userDetails) {
        return userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    /**
     * Generate token
     *
     * @param claims Data declaration
     * @return token token
     */
    private String generateToken(String username, Map<String, Object> claims) {
        Date date = new Date(System.currentTimeMillis());

        Algorithm algorithm = Algorithm.RSA256(rsaKeyProperties.getPublicKey(), rsaKeyProperties.getPrivateKey());
        return JWT.create()
                .withPayload(claims)
                .withSubject(username)
                .withIssuer("booking-meeting")
                .withJWTId(createJTI())
                .withAudience(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(date.getTime() + expiration))
                .sign(algorithm);
    }

    /**
     * Get data claim
     *
     * @param token token
     * @return Data declaration
     */
    private Map<String, Claim> getClaimsFromToken(String token) {
        Map<String, Claim> claims;
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        try {
            Algorithm algorithm = Algorithm.RSA256(rsaKeyProperties.getPublicKey(), rsaKeyProperties.getPrivateKey());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("BookingMeetingService")
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            claims = decodedJWT.getClaims();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private static String createJTI() {
        return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
    }

    public long getExpiration() {
        return expiration;
    }

    /**
     * Save Token information to Redis
     *
     * @param token    Token information
     * @param username username
     * @param deviceId deviceId
     */
    public void setTokenInfo(String token, String username, String deviceId) {
        if (StringUtils.isNotEmpty(token)) {
            LocalDateTime localDateTime = LocalDateTime.now();
            RedisUtils.hset(token, "username", username, refreshTime);
            RedisUtils.hset(token, "deviceId", deviceId, refreshTime);
            RedisUtils.hset(token, "refreshTime", df.format(localDateTime.plus(refreshTime, ChronoUnit.MILLIS)), refreshTime);
            RedisUtils.hset(token, "expiration", df.format(localDateTime.plus(expiration, ChronoUnit.MILLIS)), refreshTime);
        }
    }

    /**
     * Put Token in blacklist
     *
     * @param token Token information
     */
    public void addBlackList(String token) {
        if (StringUtils.isNotEmpty(token)) {
            RedisUtils.hset("blackList", token, df.format(LocalDateTime.now()));
        }
    }

    /**
     * Redis Delete Token in
     *
     * @param token Token information
     */
    public static void deleteRedisToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            RedisUtils.deleteKey(token);
        }
    }

    /**
     * Judge whether the current Token is in the blacklist
     *
     * @param token Token information
     */
    public static boolean isBlackList(String token) {
        if (StringUtils.isNotEmpty(token)) {
            return RedisUtils.hasKey("blackList", token);
        }
        return false;
    }

    /**
     * Is it overdue
     *
     * @param expiration Expiration time, string
     * @return Return True after expiration and false if not expired
     */
    public static boolean isExpiration(String expiration) {
        LocalDateTime expirationTime = LocalDateTime.parse(expiration, df);
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.compareTo(expirationTime) > 0;
    }

    /**
     * Is it effective
     *
     * @param refreshTime Refresh time, string
     * @return Valid return True, invalid return false
     */
    public static boolean isValid(String refreshTime) {
        LocalDateTime validTime = LocalDateTime.parse(refreshTime, df);
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.compareTo(validTime) <= 0;
    }

    /**
     * Check whether Token exists in Redis
     *
     * @param token Token information
     * @return boolean
     */
    public static boolean hasToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            return RedisUtils.hasKey(token);
        }
        return false;
    }

    /**
     * Get expiration time from Redis
     *
     * @param token Token information
     * @return Expiration time, string
     */
    public static String getExpirationByToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            return RedisUtils.hget(token, "expiration").toString();
        }
        return null;
    }

    /**
     * Get refresh time from Redis
     *
     * @param token Token information
     * @return Refresh time, string
     */
    public static String getRefreshTimeByToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            return RedisUtils.hget(token, "refreshTime").toString();
        }
        return null;
    }

    /**
     * Get user name from Redis
     *
     * @param token Token information
     * @return String
     */
    public static String getUserNameByToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            return RedisUtils.hget(token, "username").toString();
        }
        return null;
    }

    /**
     * Get IP from Redis
     *
     * @param token Token information
     * @return String
     */
    public static String getIpByToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            return RedisUtils.hget(token, "deviceId").toString();
        }
        return null;
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);
}
