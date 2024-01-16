package com.gamecommunity.domain.guestbook.controller;

import com.gamecommunity.domain.guestbook.dto.CreateGuestbookDto;
import com.gamecommunity.domain.guestbook.dto.ModifyGuestbookDto;
import com.gamecommunity.domain.guestbook.service.GuestbookService;
import com.gamecommunity.global.response.ApiResponse;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class GuestbookController {

  private final GuestbookService guestbookService;

  // 방명록 댓글 작성
  @PostMapping("/{toUserId}/guestbooks")
  public ResponseEntity<ApiResponse> createComment(
          @PathVariable Long toUserId,
          @RequestBody @Valid CreateGuestbookDto.Request createGuestbookDto,
          @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {

    guestbookService.createComment(toUserId, createGuestbookDto, userDetails);

    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("방명록 작성 성공", null));
  }

  // 방명록 댓글 수정
  @PatchMapping("/{toUserId}/guestbooks/{guestbookId}")
  public ResponseEntity<ApiResponse> modifyComment(
          @PathVariable Long guestbookId,
          @RequestBody @Valid ModifyGuestbookDto modifyGuestbookDto,
          @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    guestbookService.modifyComment(guestbookId, modifyGuestbookDto.content(), userDetails);

    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("방명록 수정 성공", null));
  }

}
