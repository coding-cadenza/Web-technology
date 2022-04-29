package com.cjf.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import javax.xml.bind.DatatypeConverter;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

public class Token {

    public static String sercetKey = "thisisblogbycjf";   // 密钥
    public final static long keeptime = 24 * 60 * 60 * 1000;   // 有效期为一天

    /**
     *
     * @param id
     * @param issuer
     * @param subject
     * @return
     */
    public static String generToken(String id, String issuer, String subject) {
        long ttlMillis = keeptime;
        //使用Hash256算法进行加密
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //获取系统时间以便设置token有效时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //将密钥转码为base64形式,再转为字节码
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(sercetKey);
        //对其使用Hash256进行加密
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        //JWT生成类,此时设置iat,以及根据传入的id设置token
        JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(now);
        //由于Payload是非必须加入的,所以这时候要加入检测
        if (subject != null) {
            builder.setSubject(subject);
        }
        if (issuer != null) {
            builder.setIssuer(issuer);
        }
        //进行签名,生成Signature
        builder.signWith(signatureAlgorithm, signingKey);
        // 设置过期时间
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        //返回最终的token结果
        return builder.compact();
    }

    /**
     * 更新token
     * @param token
     * @return
     */
    public static String updateToken(String token) {
        //Claims就是包含了我们的Payload信息类
        Claims claims = verifyToken(token);
        String id = claims.getId();
        String subject = claims.getSubject();
        String issuer = claims.getIssuer();
        //生成新的token,根据现在的时间
        return generToken(id, issuer, subject);
    }
    public  static  int getIdByToken(String token){
        Claims claims = verifyToken(token);
        int id = Integer.parseInt(claims.getId());
        return id;
    }

    /**
     * 将token解密，将payload信息包装
     * @param token
     * @return
     */
    private static Claims verifyToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(sercetKey))
                .parseClaimsJws(token).getBody();
        return claims;
    }




}
