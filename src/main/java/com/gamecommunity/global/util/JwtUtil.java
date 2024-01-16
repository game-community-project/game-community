package com.gamecommunity.global.util;

import com.gamecommunity.domain.user.dto.TokenDto;
import com.gamecommunity.domain.user.entity.UserRoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtUtil {

  public static final String Access_Header = "Access";

  public static final String Refresh_Header = "Refresh";

  public static final String BEARER_PREFIX = "Bearer ";

  // 사용자 권한 값의 KEY
  public static final String AUTHORIZATION_KEY = "auth";

  private final long TOKEN_TIME = 60 * 60 * 3 * 1000L;
  private final long REFRESH_TOKEN_TIME = 60 * 60 * 24 * 1000L;

  @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
  private String secretKey;
  private Key key;
  private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

  @PostConstruct
  public void init() {
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    key = Keys.hmacShaKeyFor(bytes);
  }

  public String createAccessToken(String email, UserRoleEnum role) {
    Date date = new Date();

    return BEARER_PREFIX +
        Jwts.builder()
            .setSubject(email)
            .claim(AUTHORIZATION_KEY, role)
            .setExpiration(new Date(date.getTime() + TOKEN_TIME))
            .setIssuedAt(date)
            .signWith(key, signatureAlgorithm)
            .compact();
  }

  public String createRefreshToken(String email, UserRoleEnum role) {
    Date date = new Date();

    return BEARER_PREFIX +
        Jwts.builder()
            .setSubject(email)
            .claim(AUTHORIZATION_KEY, role)
            .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
            .setIssuedAt(date)
            .signWith(key, signatureAlgorithm)
            .compact();
  }

  // header 에서 JWT 가져오기
  public String getJWtAccessHeader(HttpServletRequest request) {

    String bearerToken = request.getHeader(Access_Header);

    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public String getJWtRefreshHeader(HttpServletRequest request) {

    String bearerToken = request.getHeader(Refresh_Header);

    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(BEARER_PREFIX.length());
    }
    return null;
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException | SignatureException e) {
      throw new JwtException("유효하지 않는 JWT 서명 입니다.");
    } catch (ExpiredJwtException e) {
      throw new JwtException("만료된 JWT token 입니다.");
    } catch (UnsupportedJwtException e) {
      throw new JwtException("지원되지 않는 JWT 토큰 입니다.");
    } catch (IllegalArgumentException e) {
      throw new JwtException("잘못된 JWT 토큰 입니다.");
    }

  }

  // 토큰에서 사용자 정보 가져오기
  public Claims getUserInfoFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
        .getBody(); // body안에 claim기반 데이터 반환
  }

  public void setTokenResponse(TokenDto tokenDto, HttpServletResponse response) {
    setHeaderAccessToken(tokenDto.accessToken(), response);
    setHeaderRefreshToken(tokenDto.refreshToken(), response);

  }

  private void setHeaderAccessToken(String accessToken, HttpServletResponse response) {
    response.setHeader(Access_Header, accessToken);
  }

  private void setHeaderRefreshToken(String refreshToken, HttpServletResponse response) {
    response.setHeader(Refresh_Header, refreshToken);
  }

  public Long getExpiration(String refreshToken) {
    // 리프레시 토큰 만료시간
    Date expiration = Jwts.parserBuilder().setSigningKey(key).build()
        .parseClaimsJws(refreshToken.substring(BEARER_PREFIX.length())).getBody()
        .getExpiration();
    //현재시간
    long now = new Date().getTime();
    return (expiration.getTime() - now);
  }

  public UserRoleEnum getUserRole(String refreshToken) {
    Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(refreshToken).getBody();

    UserRoleEnum role = UserRoleEnum.valueOf((String) claims.get(AUTHORIZATION_KEY));

    return role;
  }

}
