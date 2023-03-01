package com.salon.ht.entity.payload;

import lombok.*;

@Data
@Getter
@Setter
public class JwtAuthenticationResponse {
    String accessToken;
    String refreshToken;
    String type;
    private long expiryDuration;

    public JwtAuthenticationResponse(String accessToken, String refreshToken, long expiryDuration) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.type = "Bearer ";
        this.expiryDuration = expiryDuration;
    }
}
