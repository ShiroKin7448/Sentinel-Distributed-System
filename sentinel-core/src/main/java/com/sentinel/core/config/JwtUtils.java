package com.sentinel.core.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // Đây là "Chìa khóa bí mật" để ký tên lên Token.
    // Trong thực tế phải giấu kỹ trong biến môi trường.
    // Chuỗi này phải đủ dài (ít nhất 256-bit) nếu không sẽ lỗi.
    private static final String SECRET_KEY = "SentinelSuperSecretKeyForDistributedSystemProjectMustBeVeryLong";

    // Thời hạn của Token (ví dụ: 24 giờ)
    private static final long EXPIRATION_MS = 86400000;

    // Hàm 1: Tạo Token từ Username
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + EXPIRATION_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Hàm 2: Lấy Username từ Token (để biết ai đang gọi API)
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // Hàm 3: Kiểm tra Token có hợp lệ không
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            System.err.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(java.util.Base64.getEncoder().encodeToString(SECRET_KEY.getBytes())));
    }
}