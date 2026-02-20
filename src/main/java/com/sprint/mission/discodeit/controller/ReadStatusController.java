package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/read-statuses")
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  // 특정 채널 메세지 수신 정보 생성
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ReadStatusDto.Response create(@RequestBody ReadStatusDto.CreateRequest request) {
    return readStatusService.create(request);
  }

  // 특정 채널 메세지 수신 정보 수정
  @PatchMapping("/{readStatusId}")
  public ReadStatusDto.Response update(
      @PathVariable UUID readStatusId,
      @RequestBody ReadStatusDto.UpdateRequest request) {
    return readStatusService.update(readStatusId, request);
  }

  // 특정 사용자 메세지 수신 정보 조회
  @GetMapping
  public List<ReadStatusDto.Response> findAllByUserId(@RequestParam UUID userId) {
    return readStatusService.findAllByUserId(userId);
  }
}
