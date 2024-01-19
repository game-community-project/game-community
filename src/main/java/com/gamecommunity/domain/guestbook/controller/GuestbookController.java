package com.gamecommunity.domain.guestbook.controller;

import com.gamecommunity.domain.guestbook.dto.CreateGuestbookDto;
import com.gamecommunity.domain.guestbook.dto.GuestbookDto;
import com.gamecommunity.domain.guestbook.dto.ModifyGuestbookDto;
import com.gamecommunity.domain.guestbook.service.GuestbookService;
import com.gamecommunity.global.aop.Timer;
import com.gamecommunity.global.response.ApiResponse;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class GuestbookController {

  private final GuestbookService guestbookService;

  // 방명록 댓글 작성
  @Timer
  @PostMapping("/{toUserId}/guestbooks")
  public ResponseEntity<ApiResponse> createComment(
          @PathVariable Long toUserId,
          @RequestBody @Valid CreateGuestbookDto.Request createGuestbookDto,
          @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {

    guestbookService.createComment(toUserId, createGuestbookDto, userDetails);

    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("방명록 작성 성공", null));
  }

  // 방명록 조회
  @GetMapping("/{toUserId}/guestbooks")
  public ResponseEntity<ApiResponse<Page<GuestbookDto>>> getComment(
          @RequestParam("page") int page,
          @RequestParam("size") int size,
          @RequestParam("sortBy") String sortBy,
          @RequestParam("isAsc") boolean isAsc,
          @PathVariable Long toUserId
  ) {
    Page<GuestbookDto> guestbookDtoList = guestbookService.getComment(page-1, size,sortBy,isAsc,toUserId);

    return ResponseEntity.ok(ApiResponse.ok("방명록 조회 성공", guestbookDtoList));
  }

  // 방명록 댓글 수정
  @Timer
  @PatchMapping("/{toUserId}/guestbooks/{guestbookId}")
  public ResponseEntity<ApiResponse> modifyComment(
          @PathVariable Long guestbookId,
          @RequestBody @Valid ModifyGuestbookDto modifyGuestbookDto,
          @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    guestbookService.modifyComment(guestbookId, modifyGuestbookDto.content(), userDetails);

    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok("방명록 수정 성공", null));
  }

  // 방명록 댓글 삭제
  @Timer
  @DeleteMapping("/{toUserId}/guestbooks/{guestbookId}")
  public ResponseEntity<ApiResponse> deleteComment(
          @PathVariable Long guestbookId,
          @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    guestbookService.deleteComment(guestbookId, userDetails);

    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok("방명록 삭제 성공", null));
  }

}
