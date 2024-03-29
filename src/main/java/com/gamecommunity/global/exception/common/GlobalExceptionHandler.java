package com.gamecommunity.global.exception.common;

import com.gamecommunity.global.response.ApiResponse;
import java.util.NoSuchElementException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({NoSuchElementException.class})
  protected ResponseEntity<ApiResponse> handlerNoSuchElementFoundException(
      NoSuchElementException ex) {
    HttpStatus hs = HttpStatus.NOT_FOUND;
    final ErrorResponse errorResponse = ErrorResponse.create(ex, hs, ex.getMessage());
    return ResponseEntity.status(hs).body(ApiResponse.fail(ex.getMessage(), errorResponse));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  protected ResponseEntity<ApiResponse> handlerIllegalArgumentException(
      IllegalArgumentException ex) {
    HttpStatus hs = HttpStatus.BAD_REQUEST;
    final ErrorResponse errorResponse = ErrorResponse.create(ex, hs, ex.getMessage());

    return ResponseEntity.status(hs).body(ApiResponse.fail(ex.getMessage(), errorResponse));
  }

  @ExceptionHandler(DuplicateKeyException.class)
  protected ResponseEntity<ApiResponse> handlerDuplicateKeyException(DuplicateKeyException ex) {
    HttpStatus hs = HttpStatus.CONFLICT;
    final ErrorResponse errorResponse = ErrorResponse.create(ex, hs, ex.getMessage());

    return ResponseEntity.status(hs).body(ApiResponse.fail(ex.getMessage(), errorResponse));
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ApiResponse> handlerException(Exception ex) {
    HttpStatus hs = HttpStatus.INTERNAL_SERVER_ERROR;
    final ErrorResponse errorResponse = ErrorResponse.create(ex, hs, ex.getMessage());

    return ResponseEntity.status(hs).body(ApiResponse.fail(ex.getMessage(), errorResponse));
  }

  @ExceptionHandler
  public ResponseEntity<ApiResponse> handlerCustomException(BusinessException ex) {
    HttpStatus hs = ex.getStatus();
    final ErrorResponse errorResponse = ErrorResponse.create(ex, hs, ex.getMessage());
    return ResponseEntity.status(hs).body(ApiResponse.fail(ex.getMessage(), errorResponse));
  }

  @ExceptionHandler
  public ResponseEntity<ApiResponse> handlerMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    HttpStatus hs = HttpStatus.BAD_REQUEST;
    final ErrorResponse errorResponse = ErrorResponse.create(ex, hs,
        ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    return ResponseEntity.status(hs).body(ApiResponse.fail(ex.getMessage(), errorResponse));
  }

}
