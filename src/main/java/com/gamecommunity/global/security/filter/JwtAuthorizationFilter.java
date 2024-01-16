package com.gamecommunity.global.security.filter;


import com.gamecommunity.global.security.userdetails.UserDetailsServiceImpl;
import com.gamecommunity.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends
    OncePerRequestFilter {  //OncePerRequestFilter 요청당  해당 필터가 한번만 수행

  private final JwtUtil jwtUtil;
  private final UserDetailsServiceImpl userDetailsService;

  public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
  }


  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
      FilterChain filterChain) throws ServletException, IOException {

    String tokenValue = jwtUtil.getJWtAccessHeader(req);

    if (StringUtils.hasText(tokenValue)) {

      try {
        jwtUtil.validateToken(tokenValue);
      } catch (JwtException e) {
        log.error(e.getMessage());
        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        res.setContentType("application/json");
        res.setCharacterEncoding("utf-8");
        PrintWriter writer = res.getWriter();
        writer.println(e.getMessage());
        return;
      }

      Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

      try {
        setAuthentication(info.getSubject());
      } catch (Exception e) {
        log.error(e.getMessage());
        return;
      }
    }

    filterChain.doFilter(req, res);
  }

  public void setAuthentication(String email) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    Authentication authentication = createAuthentication(email);
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
  }

  private Authentication createAuthentication(String email) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }

}
