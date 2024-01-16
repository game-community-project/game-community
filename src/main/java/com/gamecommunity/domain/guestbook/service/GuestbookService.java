package com.gamecommunity.domain.guestbook.service;

import com.gamecommunity.domain.guestbook.dto.CreateGuestbookDto;
import com.gamecommunity.domain.guestbook.entity.Guestbook;
import com.gamecommunity.domain.guestbook.repository.GuestbookRepository;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.domain.user.repository.UserRepository;
import com.gamecommunity.domain.user.service.UserService;
import com.gamecommunity.global.exception.common.BusinessException;
import com.gamecommunity.global.exception.common.ErrorCode;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuestbookService {

  private final UserRepository userRepository;

  private final GuestbookRepository guestbookRepository;


  public void createComment(
          Long toUserId,
          CreateGuestbookDto.Request createGuestBookDto,
          UserDetailsImpl userDetails
  ) {

    User toUser = userRepository.findById(toUserId).orElseThrow(() ->
            new BusinessException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_USER_EXCEPTION));
    User fromUser = userDetails.getUser();

    Guestbook guestBook = createGuestBookDto.toEntity(createGuestBookDto.content(), toUser, fromUser);

    guestbookRepository.save(guestBook);

  }


}
