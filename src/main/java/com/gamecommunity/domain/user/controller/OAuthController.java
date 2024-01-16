package com.gamecommunity.domain.user.controller;

import com.gamecommunity.domain.user.dto.TokenDto;
import com.gamecommunity.domain.user.service.OAuthService;
import com.gamecommunity.global.response.ApiResponse;
import com.gamecommunity.global.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
  public ResponseEntity<ApiResponse> kakaoLogin(@RequestParam String code,
      HttpServletResponse response) throws JsonProcessingException {

    TokenDto tokenDto = oauthService.kakaoLogin(code);
    jwtUtil.setTokenResponse(tokenDto, response);

    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("카카오 로그인 성공.",null));
  }

}
