package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponseDto> handleDiscodeitException(DiscodeitException e,
      HttpServletRequest request) {

    ErrorCode errorCode = e.getErrorCode();

    log.error("[DiscodeitException] Path={}, Code={}, Message={}, Details={}",
        request.getRequestURI(), errorCode.getCode(), e.getMessage(), e.getDetails());

    return buildResponse(
        errorCode.getStatus(),
        errorCode.getCode(),
        errorCode.getMessage(),
        e.getDetails(),
        e.getClass().getSimpleName(),
        e.getTimestamp(),
        request
    );
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e,
      HttpServletRequest request) {

    log.warn("[Method Argument Not Valid Exception] Path={}, Message={}",
        request.getRequestURI(), e.getMessage());

    Map<String, Object> details = new HashMap<>();
    e.getBindingResult().getFieldErrors().forEach(error ->
        details.put(error.getField(), error.getDefaultMessage()));

    ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;

    return buildResponse(
        HttpStatus.BAD_REQUEST,
        errorCode.getCode(),
        "입력 값이 올바르지 않습니다.",
        details,
        e.getClass().getSimpleName(),
        Instant.now(),
        request
    );
  }

  @ExceptionHandler({RuntimeException.class, Exception.class})
  public ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException e,
      HttpServletRequest request) {

    log.error("[Unhandled Exception] ", e);

    ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;

    return buildResponse(
        errorCode.getStatus(),
        errorCode.getCode(),
        e.getMessage() != null ? e.getMessage() : errorCode.getMessage(),
        null,
        e.getClass().getSimpleName(),
        Instant.now(),
        request
    );
  }

  private ResponseEntity<ErrorResponseDto> buildResponse(
      HttpStatus status,
      String code,
      String message,
      Map<String, Object> details,
      String exceptionType,
      Instant timestamp,
      HttpServletRequest request) {

    ErrorResponseDto response = ErrorResponseDto.builder()
        .timestamp(timestamp)
        .status(status.value())
        .code(code)
        .message(message)
        .details(details)
        .exceptionType(exceptionType)
        .path(request.getRequestURI())
        .build();

    return new ResponseEntity<>(response, status);
  }
}