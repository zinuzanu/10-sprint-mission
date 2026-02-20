package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  // 유저 등록
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public UserDto.Response create(
      @RequestPart("userCreateRequest") UserDto.CreateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {
    return userService.create(request, profile);
  }

  // 유저 정보 수정
  @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public UserDto.Response update(
      @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UserDto.UpdateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profile) {
    return userService.update(userId, request, profile);
  }

  // 유저 삭제
  @DeleteMapping("/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID userId) {
    userService.delete(userId);
  }

  // 모든 유저 조회
  @GetMapping
  public ResponseEntity<List<UserDto.Response>> findAll() {
    return ResponseEntity.ok(userService.findAll());
  }

  // 유저 온라인 상태 업데이트
  @PatchMapping("/{userId}/userStatus")
  public UserStatusDto.Response updateUserStatusByUserId(
      @PathVariable UUID userId,
      @RequestBody UserStatusDto.UpdateRequest request) {
    return userStatusService.update(userId, request);
  }
}
