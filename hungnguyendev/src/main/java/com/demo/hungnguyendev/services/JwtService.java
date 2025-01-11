package com.demo.hungnguyendev.services;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function; // Added import for Function

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.demo.hungnguyendev.config.jwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final jwtConfig jwtConfig;
    private final Key key;

    public JwtService(jwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(jwtConfig.getSecretKey().getBytes()));
    }

    public String generateToken(Long userId, String email){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpirationTime());

        return Jwts.builder()
            .setSubject(String.valueOf(userId))
            .claim("email", email)
            .setIssuer(jwtConfig.getIssuer())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }

    public String getUserIdFromJwt(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        return claims.getSubject();
    }

    public String getEmailFromJwt(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.get("email", String.class);
    }

    public boolean isValidToken(String token, UserDetails userDetails){
        
        try {

            if(!isTokenFormatValid(token)){
                return false;
            }
            if(!isSignatureValid(token)){
                return false;
            }
            if(!isTokenExpired(token)){
                return false;
            }
            if(!isIssuerToken(token)){
                return false;
            }

            final String emailFromToken = getEmailFromJwt(token);
            if(!emailFromToken.equals(userDetails.getUsername())){
                return false;
            }
            
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    public boolean isTokenFormatValid(String token){
        
        try {

            String[] tokenParts = token.split("\\.");
            return tokenParts.length == 3;
            
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSignatureValid(String token){
        try {
            
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public Key getSigningKey(){
        byte[] keyBytes = jwtConfig.getSecretKey().getBytes();
        return Keys.hmacShaKeyFor(Base64.getEncoder().encode(keyBytes));
    }

    public boolean isTokenExpired(String token){
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    }

    public boolean isIssuerToken(String token){
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.after(new Date());
    }
        
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
}