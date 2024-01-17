package com.gamecommunity.domain.user.service;

import com.gamecommunity.domain.user.dto.UserProfileDto;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.domain.user.repository.UserRepository;
import com.gamecommunity.global.exception.common.BusinessException;
import com.gamecommunity.global.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {
  private final UserRepository userRepository;

  @Transactional
  public UserProfileDto getProfile(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(() ->
            new BusinessException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_USER_EXCEPTION));

    return UserProfileDto.from(user);
  }

}
