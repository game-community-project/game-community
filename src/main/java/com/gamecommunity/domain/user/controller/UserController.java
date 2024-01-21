package com.gamecommunity.domain.user.controller;

import com.gamecommunity.domain.user.dto.EmailDto;
import com.gamecommunity.domain.user.dto.LoginRequestDto;
import com.gamecommunity.domain.user.dto.PasswordChangeRequestDto;
import com.gamecommunity.domain.user.dto.SignupRequestDto;
import com.gamecommunity.domain.user.dto.TokenDto;
import com.gamecommunity.domain.user.service.UserService;
import com.gamecommunity.global.response.ApiResponse;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import com.gamecommunity.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private final JwtUtil jwtUtil;

  // 회원가입
  @PostMapping("/signup")
  public ResponseEntity<ApiResponse> signup(@Valid @RequestBody SignupRequestDto requestDto) {

    userService.signup(requestDto);

    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("회원가입 성공.", null));
  }

  // 이메일 발송
  @PostMapping("/signup/email")
  public ResponseEntity<ApiResponse> emailSend(@Valid @RequestBody EmailDto.Request requestDto) {

    userService.emailSend(requestDto.email());

    return ResponseEntity.ok(ApiResponse.ok("이메일 인증번호 발송 성공.", null));
  }

  // 이메일 인증 확인
  @PostMapping("/signup/emailauthcheck")
  public ResponseEntity<ApiResponse> emailAuthCheck(
      @Valid @RequestBody EmailDto.CheckRequest requestDto) {

    userService.emailAuthCheck(requestDto);

    return ResponseEntity.ok(ApiResponse.ok("이메일 인증 성공.", null));
  }


  //로그인
  @PostMapping("/login")
  public ResponseEntity<ApiResponse> login(
      @Valid @RequestBody LoginRequestDto requestDto,
      HttpServletResponse response) {

    TokenDto tokenDto = userService.login(requestDto);

    jwtUtil.setTokenResponse(tokenDto, response);

    return ResponseEntity.ok(ApiResponse.ok("로그인 성공.", null));
  }

  // 비밀번호 변경
  @PutMapping("/password")
  public ResponseEntity<ApiResponse> updatePassword(
      @Valid @RequestBody PasswordChangeRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    userService.updatePassword(requestDto, userDetails.getUser().getId());

    return ResponseEntity.ok(ApiResponse.ok("비밀번호 변경 성공.", null));
  }

  // 로그아웃
  @DeleteMapping("/logout")
  public ResponseEntity<ApiResponse> logout(HttpServletRequest request) {

    userService.logout(request);

    return ResponseEntity.ok(ApiResponse.ok("로그아웃 성공.", null));
  }

  // 토큰 재발급
  @PostMapping("/reissue")
  public ResponseEntity<ApiResponse> reissue(HttpServletRequest request,
      HttpServletResponse response) {

    TokenDto tokenDto = userService.reissue(request);

    jwtUtil.setTokenResponse(tokenDto, response);

    return ResponseEntity.ok(ApiResponse.ok("토큰 재발급 성공.", null));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<ApiResponse> getUser(
          @PathVariable Long userId
  ) {
    return ResponseEntity.ok(ApiResponse.ok("유저 조회 성공", userService.getUser(userId)));
  }

}
