package com.salon.ht.config;

import com.salon.ht.util.RSAUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Data
@Component
@ConfigurationProperties(prefix = "rsa.key")
public class RSAKeyProperties {
    private String publicKeyFile;
    private String privateKeyFile;

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
    private String secret;

    @PostConstruct
    public void createRSAKey() throws Exception {
        RSAUtils.generateKey(publicKeyFile, privateKeyFile, secret, 0);
        this.publicKey = (RSAPublicKey) RSAUtils.getPublicKey(publicKeyFile);
        this.privateKey = (RSAPrivateKey) RSAUtils.getPrivateKey(privateKeyFile);
    }
}
