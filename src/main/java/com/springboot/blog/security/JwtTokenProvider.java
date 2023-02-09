package com.springboot.blog.security;

import com.springboot.blog.exception.BlogAPIException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // fetch the value from application.yml
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expireTime}")
    private long jwtExpirationDate;


    public String generateJwtToken(Authentication authentication) {

        String usernameOrEmail = authentication.getName();

        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .setSubject(usernameOrEmail)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(decodeJwtSecret())
                .compact();
    }



    public String getUsernameOrEmailFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(decodeJwtSecret())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }



    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(decodeJwtSecret())
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        } catch (ExpiredJwtException e) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        } catch (UnsupportedJwtException e) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty");
        }
    }



    private Key decodeJwtSecret() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }



}
