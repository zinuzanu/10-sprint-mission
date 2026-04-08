package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelCreatePrivateRequest;
import com.sprint.mission.discodeit.dto.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "2. 채팅 관리", description = "채널 생성, 수정, 삭제 및 조회 API")
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  @Operation(summary = "공개 채널 생성", description = "누구나 참여할 수 있는 공개 채널을 생성합니다. (생성자 정보 기록 로직 추가 예정)")
  @PostMapping("/public")
  @ResponseStatus(HttpStatus.CREATED)
  public ChannelDto createPublicChannel(
      @Valid @RequestBody ChannelCreatePublicRequest createPublicRequest) {

    log.info("[REQUEST] Create Public Channel: name={}", createPublicRequest.getName());

    return channelService.createPublicChannel(createPublicRequest);
  }

  @Operation(summary = "비공개 채널 생성", description = "특정 사용자들만 참여하는 비공개 채널을 생성합니다.")
  @PostMapping("/private")
  @ResponseStatus(HttpStatus.CREATED)
  public ChannelDto createPrivateChannel(
      @Valid @RequestBody ChannelCreatePrivateRequest createPrivateRequest) {

    log.info("[REQUEST] Create Private Channel: participantIds={}",
        createPrivateRequest.getParticipantIds());

    return channelService.createPrivateChannel(createPrivateRequest);
  }

  @Operation(summary = "채널 정보 수정", description = "기존 채널의 이름이나 설명을 수정합니다.")
  @PatchMapping("/{channelId}")
  public ChannelDto update(
      @Parameter(description = "수정할 채널의 UUID", required = true)
      @PathVariable UUID channelId,
      @Valid @RequestBody ChannelUpdateRequest request) {

    log.info("[REQUEST] Update Channel: id={}", channelId);

    return channelService.update(channelId, request);
  }

  @Operation(summary = "채널 삭제", description = "특정 채널을 시스템에서 삭제합니다.")
  @DeleteMapping("/{channelId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(
      @Parameter(description = "삭제할 채널의 UUID", required = true)
      @PathVariable UUID channelId) {

    log.info("[REQUEST] Delete Channel: id={}", channelId);

    channelService.delete(channelId);
  }

  @Operation(summary = "유저별 채널 목록 조회", description = "특정 유저가 참여하고 있는 모든 채널 목록을 가져옵니다.")
  @GetMapping
  public List<ChannelDto> getChannelsByUserId(
      @Parameter(description = "조회할 유저의 UUID", required = true)
      @RequestParam UUID userId) {
    return channelService.findAllByUserId(userId);
  }
}
