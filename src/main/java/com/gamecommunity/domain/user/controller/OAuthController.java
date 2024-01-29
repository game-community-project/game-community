package com.gamecommunity.domain.user.controller;

import com.gamecommunity.domain.user.dto.TokenDto;
import com.gamecommunity.domain.user.service.OAuthService;
import com.gamecommunity.global.response.ApiResponse;
import com.gamecommunity.global.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class OAuthController {

  private final OAuthService oauthService;
  private final JwtUtil jwtUtil;

  @GetMapping("/kakao/callback")
  public ResponseEntity<?> kakaoCallback(@RequestParam String code,
      HttpServletResponse response) throws JsonProcessingException, UnsupportedEncodingException {

    TokenDto tokenDto = oauthService.kakaoLogin(code);
    jwtUtil.setTokenResponse(tokenDto, response);

    String access = URLEncoder.encode(tokenDto.accessToken(), "utf-8").replaceAll("\\+", "%20");
    String refresh = URLEncoder.encode(tokenDto.refreshToken(), "utf-8").replaceAll("\\+", "%20");

    HttpHeaders headers = new HttpHeaders();
//    headers.setLocation(URI.create("https://game-community-project.github.io/front-end/oauth?access="+access+"&refresh="+refresh+""));
    headers.setLocation(URI.create("https://game-community-project.github.io/front-end/?access="+access+"&refresh="+refresh+""));
    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
  }
}
