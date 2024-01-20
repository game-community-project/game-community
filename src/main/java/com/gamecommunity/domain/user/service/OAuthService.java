
package com.gamecommunity.domain.user.service;

import com.gamecommunity.domain.user.dto.KakaoUserInfoDto;
import com.gamecommunity.domain.user.dto.TokenDto;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.domain.user.entity.UserRoleEnum;
import com.gamecommunity.domain.user.repository.UserRepository;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import com.gamecommunity.global.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Service
public class OAuthService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;
  private final RestTemplate restTemplate;

  @Value("${kakao.rest_api_key}")
  private String KakaoRestApiKey;

  @Value("${kakao.redirect_uri}")
  private String KakaoRedirectUri;

  public TokenDto kakaoLogin(String code) throws JsonProcessingException {
    // 1. "인가 코드"로 "액세스 토큰" 요청
//    String accessToken = (code);
    String accessToken = getToken(code);

    // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
    KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

    // 3. 필요시에 회원가입
    User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

    Authentication authentication = createAuthentication(kakaoUser);
    setAuthentication(authentication);

    TokenDto tokenDto = TokenDto.of(
        jwtUtil.createAccessToken(kakaoUser.getEmail(), kakaoUser.getRole()),
        jwtUtil.createRefreshToken(kakaoUser.getEmail(), kakaoUser.getRole()));

    return tokenDto;
  }

  private String getToken(String code) throws JsonProcessingException {
    // 요청 URL 만들기
    URI uri = UriComponentsBuilder
        .fromUriString("https://kauth.kakao.com")
        .path("/oauth/token")
        .encode()
        .build()
        .toUri();

    // HTTP Header 생성
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

    // HTTP Body 생성
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "authorization_code");
    body.add("client_id", KakaoRestApiKey);
    body.add("redirect_uri", KakaoRedirectUri);
    body.add("code", code);

    RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
        .post(uri)
        .headers(headers)
        .body(body);

    // HTTP 요청 보내기
    ResponseEntity<String> response = restTemplate.exchange(
        requestEntity,
        String.class
    );

    // HTTP 응답 (JSON) -> 액세스 토큰 파싱
    JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
    return jsonNode.get("access_token").asText(); // asText()는 string형
  }

  private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
    // 요청 URL 만들기
    URI uri = UriComponentsBuilder
        .fromUriString("https://kapi.kakao.com")
        .path("/v2/user/me")
        .encode()
        .build()
        .toUri();

    // HTTP Header 생성
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + accessToken);
    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

    RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
        .post(uri) //post방식
        .headers(headers)
        .body(new LinkedMultiValueMap<>());

    // HTTP 요청 보내기
    ResponseEntity<String> response = restTemplate.exchange(
        requestEntity,
        String.class
    );

    JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
    Long id = jsonNode.get("id").asLong();
    String nickname = jsonNode.get("properties")
        .get("nickname").asText();
    String email = jsonNode.get("kakao_account")
        .get("email").asText();

    return new KakaoUserInfoDto(id, nickname, email);
  }

  private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
    // DB 에 중복된 Kakao Id 가 있는지 확인
    Long kakaoId = kakaoUserInfo.id();
    User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);

    if (kakaoUser == null) {
      // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
      String kakaoEmail = kakaoUserInfo.email();
      User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
      if (sameEmailUser != null) {
        kakaoUser = sameEmailUser;
        // 기존 회원정보에 카카오 Id 추가
        kakaoUser.addKakaoId(kakaoId);
      } else {
        // 신규 회원가입
        // password: random UUID
        String password = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(password);

        kakaoUser = User.builder()
            .email(kakaoUserInfo.email())
            .password(encodedPassword)
            .nickname(kakaoUserInfo.nickname())
            .role(UserRoleEnum.USER)
            .kakaoId(kakaoId)
            .build();
      }

      userRepository.save(kakaoUser);
    }
    return kakaoUser;
  }

  private Authentication createAuthentication(User user) {
    UserDetailsImpl userDetails = new UserDetailsImpl(user);
    return new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(),
        userDetails.getAuthorities());
  }

  private void setAuthentication(Authentication authentication) {
    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(authentication);
  }

}

