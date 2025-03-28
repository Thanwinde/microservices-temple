package com.microservices.user.util;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.microservices.user.config.JWTConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class JWTTool {

    private final JWTConfig jwtConfig;
    //获取jwt配置(公钥，密钥)

    private JWTSigner jwtSigner;
    //获取jwt签名，签名是jwt的基石

    public KeyPair generateKeyPair(){
        byte[] bytePublicKey = Base64.getDecoder().decode(jwtConfig.getPublicKey());

        byte[] bytePrivateKey = Base64.getDecoder().decode(jwtConfig.getPrivateKey());

        //把公钥私钥转成字节码

        X509EncodedKeySpec X509keySpec = new X509EncodedKeySpec(bytePublicKey);

        PKCS8EncodedKeySpec PKCS8keySpec = new PKCS8EncodedKeySpec(bytePrivateKey);

        //采用不同的加密算法进行加密

        PublicKey publicKey1 = null;
        PrivateKey privateKey1 = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            publicKey1 = keyFactory.generatePublic(X509keySpec);

            privateKey1 = keyFactory.generatePrivate(PKCS8keySpec);
            //尝试生成公钥私钥对象
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("JWT生成出错:" + e.getMessage());
            throw new RuntimeException(e);
        }

        return new KeyPair(publicKey1,privateKey1);
        //返回处理好的公私钥对象
    }

    @PostConstruct
    public void init(){
        this.jwtSigner = JWTSignerUtil.createSigner("rs256", generateKeyPair());
    }
    //初始化，先生成签名对象

    public String createToken(Long userId, Duration ttl) {
        //构造jwt
        this.jwtSigner = JWTSignerUtil.createSigner("rs256", generateKeyPair());
        return JWT.create()
                .setPayload("user", userId)
                .setExpiresAt(new Date(System.currentTimeMillis() + ttl.toMillis()))
                .setSigner(jwtSigner)
                .sign();
    }

    /**
     * 解析token
     *
     * @param token token
     * @return 解析刷新token得到的用户信息
     */
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