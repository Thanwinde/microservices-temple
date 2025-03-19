package com.microservices.gateway.util;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JWTTool {
    @Value("${rsa.privateKey}")
    String privateKey;
    @Value("${rsa.publicKey}")
    String publicKey;

    private JWTSigner jwtSigner;

    public KeyPair generateKeyPair(){
        byte[] bytePublicKey = Base64.getDecoder().decode(publicKey);

        byte[] bytePrivateKey = Base64.getDecoder().decode(privateKey);

        X509EncodedKeySpec X509keySpec = new X509EncodedKeySpec(bytePublicKey);

        PKCS8EncodedKeySpec PKCS8keySpec = new PKCS8EncodedKeySpec(bytePrivateKey);

        PublicKey publicKey1 = null;
        PrivateKey privateKey1 = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            publicKey1 = keyFactory.generatePublic(X509keySpec);

            privateKey1 = keyFactory.generatePrivate(PKCS8keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("JWT生成出错:" + e.getMessage());
            throw new RuntimeException(e);
        }

        return new KeyPair(publicKey1,privateKey1);
    }

    public JWTTool() {

    }
    @PostConstruct
    public void init(){
        this.jwtSigner = JWTSignerUtil.createSigner("rs256", generateKeyPair());
    }

    public String createToken(Long userId, Duration ttl) {
        //生成jwt
        return JWT.create()
                .setPayload("user", userId)
                .setExpiresAt(new Date(System.currentTimeMillis() + ttl.toMillis()))
                .setSigner(jwtSigner)
                .sign();
    }

    public Long parseToken(String token) {
        // 1.校验token是否为空
        if (token == null) {
            throw new ValidateException("token为空");
        }
        // 2.校验并解析jwt
        JWT jwt;
        try {
            jwt = JWT.of(token).setSigner(jwtSigner);
        } catch (Exception e) {
            throw new ValidateException("无效的token", e);
        }
        // 2.校验jwt是否有效
        if (!jwt.verify()) {
            // 验证失败
            throw new ValidateException("无效的token");
        }
        // 3.校验是否过期
        try {
            JWTValidator.of(jwt).validateDate();
        } catch (ValidateException e) {
            throw new ValidateException("token已经过期");
        }
        // 4.数据格式校验
        Object userPayload = jwt.getPayload("user");
        if (userPayload == null) {
            // 数据为空
            throw new ValidateException("无效的token");
        }

        // 5.数据解析
        try {
           return Long.valueOf(userPayload.toString());
        } catch (RuntimeException e) {
            // 数据格式有误
            throw new ValidateException("无效的token");
        }
    }
}