package com.example.smart_elderly_care.jwt;

import com.example.smart_elderly_care.exception.CareClientException;
import com.example.smart_elderly_care.exception.code.ErrorStatus;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtToken generateToken(Authentication authentication) {
        CustomMemberDetails customMemberDetails = (CustomMemberDetails) authentication.getPrincipal();
        String role = "ROLE_" + customMemberDetails.getMember().getRole().name();

        long now = System.currentTimeMillis();
        Date issuedAt = new Date();

        Map<String, Object> headers = createHeaders();

        String accessToken = Jwts.builder()
                .setHeader(headers)
                .setSubject("accessToken")
                .claim("iss", "smart_elderly_care")
                .claim("userId", customMemberDetails.getMember().getId())
                .claim("phone", customMemberDetails.getMember().getPhone())
                .claim("role", role)
                .setExpiration(new Date(now + 30 * 60 * 1000L)) // 30분
                .setIssuedAt(issuedAt)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeader(headers)
                .setSubject("refreshToken")
                .claim("iss", "smart_elderly_care")
                .claim("userId", customMemberDetails.getMember().getId())
                .claim("add", "ref")
                .setExpiration(new Date(now + 7 * 24 * 60 * 60 * 1000L)) // 7일
                .setIssuedAt(issuedAt)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        if (claims.get("role") == null || claims.get("userId") == null) {
            throw new CareClientException(ErrorStatus.AUTH_INVALID_TOKEN);
        }

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(claims.get("role").toString())
        );

        Long userId = Long.parseLong(claims.get("userId").toString());

        UserDetails userDetails = new User(userId.toString(), "", authorities);

        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.warn("유효하지 않은 JWT 서명입니다: {}", e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.warn("지원하지 않는 JWT 토큰입니다: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.warn("잘못된 JWT 토큰입니다 (빈 값 또는 null): {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("알 수 없는 JWT 예외 발생: {}", e.getMessage());
            return false;
        }
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private static Map<String, Object> createHeaders() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        headers.put("typ", "JWT");
        return headers;
    }
}
