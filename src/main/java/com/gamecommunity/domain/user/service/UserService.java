package com.gamecommunity.domain.user.service;


import com.gamecommunity.domain.user.dto.EmailDto.CheckRequest;
import com.gamecommunity.domain.user.dto.LoginRequestDto;
import com.gamecommunity.domain.user.dto.PasswordChangeRequestDto;
import com.gamecommunity.domain.user.dto.SignupRequestDto;
import com.gamecommunity.domain.user.dto.TokenDto;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.domain.user.entity.UserRoleEnum;
import com.gamecommunity.domain.user.repository.UserRepository;
import com.gamecommunity.global.exception.common.BusinessException;
import com.gamecommunity.global.exception.common.ErrorCode;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import com.gamecommunity.global.util.JwtUtil;
import com.gamecommunity.global.util.RandomNumber;
import com.gamecommunity.global.util.RedisUtil;
import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;
  private final JavaMailSender mailSender;

  private final JwtUtil jwtUtil;
  private final RedisUtil redisUtil;

  @Value("${admin.token}")
  private String ADMIN_TOKEN;

  @Value("${spring.mail.username}")
  private String senderEmail;

  public void signup(SignupRequestDto requestDto) {
    if (redisUtil.getData(requestDto.email()) == null) {
      throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.EMAIL_VERIFICATION_NEEDED);
    }

    idEmailUnique(requestDto.email());
    isNicknameUnique(requestDto.nickname());
    confirmPassword(requestDto.password(), requestDto.checkPassword());

    UserRoleEnum role = UserRoleEnum.USER;
    if (requestDto.isAdmin()) {
      if (!ADMIN_TOKEN.equals(requestDto.adminToken())) {
        throw new BusinessException(HttpStatus.BAD_REQUEST,
            ErrorCode.FAILED_ADMIN_PASSWORD_EXCEPTION);
      }
      role = UserRoleEnum.ADMIN;
    }

    User user = requestDto.toEntity(passwordEncoder.encode(requestDto.password()), role);

    userRepository.save(user);
  }


  public void emailSend(String toEmail) {
    //JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
    MimeMessage message = mailSender.createMimeMessage();
    String authNumber = RandomNumber.createNumber();
    String content = "회원 가입 인증 번호: " + authNumber;

    try {
      // true를 전달하여 multipart 형식의 메시지를 지원, "utf-8"을 전달하여 문자 인코딩을 설정
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

      helper.setFrom(this.senderEmail);//이메일의 발신자 주소 설정
      helper.setTo(toEmail);//이메일의 수신자 주소 설정
      helper.setSubject("게임 커뮤니티 회원 가입 인증 이메일");//이메일의 제목을 설정
      helper.setText(content, true);//이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정으로한다.
      mailSender.send(message);
    } catch (MessagingException e) {
      //이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
      throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.FAILED_EMAIL_SEND_EXCEPTION);
    }

    redisUtil.setDataExpire(toEmail, authNumber, 60 * 5L); //5분후 만료
  }

  public void emailAuthCheck(CheckRequest requestDto) {
    String RedisAuthNumber = redisUtil.getData(requestDto.email());

    if (RedisAuthNumber.equals(requestDto.authNum())) {
      redisUtil.deleteData(requestDto.email());
      // 이메일 인증 완료 했는데 회원가입 요청을 3시간동안 안하면 다시 이메일 인증하도록 하기
      redisUtil.setDataExpire(requestDto.email(), "true", 60 * 60 * 3L);
    } else {
      throw new BusinessException(HttpStatus.BAD_REQUEST,
          ErrorCode.FAILED_EMAIL_AUTHENTICATION_EXCEPTION);
    }
  }


  public TokenDto login(LoginRequestDto requestDto) {
    String email = requestDto.email();
    String password = requestDto.password();

    User user = userRepository.findByEmail(email).orElseThrow(
        () -> new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER_EXCEPTION));

    Authentication authentication = createAuthentication(password, user);
    setAuthentication(authentication);

    TokenDto tokenDto = TokenDto.of(jwtUtil.createAccessToken(email, user.getRole()),
        jwtUtil.createRefreshToken(email, user.getRole()));

    // 레디스에서 리프레시 토큰 ttl 적용하기 위해
    Long refreshExpiration = jwtUtil.getExpiration(tokenDto.refreshToken());

    // 리프레시 토큰 ttl
    redisUtil.setDataExpire(email, tokenDto.refreshToken(), refreshExpiration);

    return tokenDto;
  }

  @Transactional
  public void updatePassword(PasswordChangeRequestDto requestDto, Long userId) {
    String password = passwordEncoder.encode(requestDto.newPassword());

    User user = userRepository.findById(userId).orElseThrow(() ->
        new BusinessException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_USER_EXCEPTION)
    );

    //로그인중 유저 패스워드랑 request에 담긴 변경전 패스워드랑 같은지 체크
    if (!(passwordEncoder.matches(requestDto.nowPassword(), user.getPassword()))) {
      throw new BusinessException(HttpStatus.BAD_REQUEST,
          ErrorCode.FAILED_AUTHENTICATION_EXCEPTION);
    }

    //변경할 비번,비번 확인 같은지 체크
    confirmPassword(requestDto.newPassword(), requestDto.checkPassword());

    user.updatePassword(password);
  }

  public void logout(HttpServletRequest request) {
    String accessToken = jwtUtil.getJWtAccessHeader(request);

    if (!jwtUtil.validateToken(accessToken)) {
      throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_TOKEN_EXCEPTION);
    }
    Claims claims = jwtUtil.getUserInfoFromToken(accessToken);
    String email = claims.getSubject();

    redisUtil.deleteData(email);
  }

  public TokenDto reissue(HttpServletRequest request) {
    String RefreshToken = jwtUtil.getJWtRefreshHeader(request);
    UserRoleEnum role = jwtUtil.getUserRole(RefreshToken);

    if (!jwtUtil.validateToken(RefreshToken)) {
      throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_TOKEN_EXCEPTION);
    }

    Claims claims = jwtUtil.getUserInfoFromToken(RefreshToken);
    String email = claims.getSubject();

    // 레디스에 해당 key:email,value:리프레시토큰 있나 확인
    if (redisUtil.getData(email) == null) {
      throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.LOGIN_REQUIRED_EXCEPTION);
    }
    // 리프레시토큰은 새로 갱신 안하고 그대로 반환 (로그아웃 말고는 24시간후 무조건 다시 로그인 하기)
    TokenDto tokenDto = TokenDto.of(jwtUtil.createAccessToken(email, role),
        request.getHeader("Refresh"));

    return tokenDto;
  }


  public void idEmailUnique(String email) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new BusinessException(HttpStatus.CONFLICT,
          ErrorCode.ALREADY_EXIST_USER_EMAIL_EXCEPTION);
    }
  }

  private void isNicknameUnique(String nickname) {
    if (userRepository.findByNickname(nickname).isPresent()) {
      throw new BusinessException(HttpStatus.CONFLICT,
          ErrorCode.ALREADY_EXIST_USER_NICKNAME_EXCEPTION);
    }
  }

  public void confirmPassword(String password, String checkPassword) {
    if (!(password.equals(checkPassword))) {
      throw new BusinessException(HttpStatus.BAD_REQUEST,
          ErrorCode.NOT_EQUALS_CONFIRM_PASSWORD_EXCEPTION);
    }
  }


  private Authentication createAuthentication(String password, User user) {
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new BusinessException(HttpStatus.BAD_REQUEST,
          ErrorCode.FAILED_AUTHENTICATION_EXCEPTION);
    }

    UserDetailsImpl userDetails = new UserDetailsImpl(user);
    return new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(),
        userDetails.getAuthorities());
  }

  private void setAuthentication(Authentication authentication) {
    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(authentication);
  }

}
