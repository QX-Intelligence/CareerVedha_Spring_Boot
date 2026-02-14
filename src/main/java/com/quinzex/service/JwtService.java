package com.quinzex.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Set;

@Service
public class JwtService {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    private static final long ACCESS_EXP = 10 * 60 * 1000;   // 10 min


    public JwtService(  @Value("${jwt.rsa.private-key}") String privateKeyStr,
                        @Value("${jwt.rsa.public-key}") String publicKeyStr)throws Exception{


        this.privateKey = loadPrivateKey(privateKeyStr);
        this.publicKey = loadPublicKey(publicKeyStr);

    }

    public String generateAccessToken(String email, Set<String> roles,Set<String> permissions,int tokenVersion,int roleVersion){
        return Jwts.builder()
                .setSubject(email)
                .claim("roles",roles)
                .claim("permissions",permissions)
                .claim("tokenVersion",tokenVersion)
                .claim("type","ACCESS")
                .claim("roleVersion",roleVersion)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ACCESS_EXP))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }


    public Claims parseToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private PublicKey loadPublicKey(String key) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(key);
        return KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(decoded));
    }

    private PrivateKey loadPrivateKey(String key) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(key);
        return KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(decoded));
    }
    public String generateServiceToken() {
        return Jwts.builder()
                .setSubject("spring-service")
                .claim("type", "SERVICE")
                .claim("scope", "INTERNAL")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) // 5 min
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }
}
