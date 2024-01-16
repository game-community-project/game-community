package com.gamecommunity.domain.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class ControllerTest implements UserTest {

  // 이 컨텍스트는 웹 애플리케이션의 빈들과 설정 정보 등을 관리하는데 사용
  @Autowired
  private WebApplicationContext context;

  // MockMvc는 웹 애플리케이션을 실제로 실행하지 않고도 컨트롤러를 테스트할 수 있는 도구
  @Autowired
  protected MockMvc mockMvc;

  // JSON 데이터를 Java 객체로 변환하거나 반대로 변환할 때 사용되는 Jackson 라이브러리의 ObjectMapper를 주입
  @Autowired
  protected ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(context)
        .build();

    // Mock 테스트 UserDetails 생성
    UserDetailsImpl testUserDetails = new UserDetailsImpl(TEST_USER);

    // SecurityContext 에 인증된 사용자 설정
    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
        testUserDetails, testUserDetails.getPassword(), testUserDetails.getAuthorities()));
  }
}
