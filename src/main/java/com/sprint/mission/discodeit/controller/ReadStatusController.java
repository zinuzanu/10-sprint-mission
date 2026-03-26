package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "2. 채팅 관리", description = "사용자의 채널별 메시지 읽음 시점(수신 정보) 관리 및 조회 API")
@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @Operation(summary = "특정 채널 메세지 수신 정보 생성", description = "특정 채널에 대한 사용자의 메시지 수신(읽음) 정보를 생성합니다.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ReadStatusDto create(
      @Valid @RequestBody ReadStatusCreateRequest request) {
    return readStatusService.create(request);
  }

  @Operation(summary = "특정 채널 메세지 수신 정보 수정", description = "사용자가 메시지를 어디까지 읽었는지(마지막 읽은 시각)를 수정합니다.")
  @PatchMapping("/{readStatusId}")
  public ReadStatusDto update(
      @Parameter(description = "수정할 수신 정보의 UUID", required = true)
      @PathVariable UUID readStatusId,
      @Valid @RequestBody ReadStatusUpdateRequest request) {
    return readStatusService.update(readStatusId, request);
  }

  @Operation(summary = "특정 사용자 메세지 수신 정보 조회", description = "특정 사용자가 참여 중인 모든 채널의 읽음 상태 정보를 조회합니다.")
  @GetMapping
  public List<ReadStatusDto> findAllByUserId(
      @Parameter(description = "조회 대상 사용자의 UUID", required = true)
      @RequestParam UUID userId) {
    return readStatusService.findAllByUserId(userId);
  }
}
