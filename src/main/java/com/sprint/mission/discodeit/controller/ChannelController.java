package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  // 공개 채널 생성
  // TODO: 채널 생성자 정보가 없으므로 로직 추가 예정. (채널을 누가 만들었는지 모름)
  @PostMapping("/public")
  @ResponseStatus(HttpStatus.CREATED)
  public ChannelDto.Response createPublicChannel(
      @RequestBody ChannelDto.CreatePublicRequest createPublicRequest) {
    return channelService.createPublicChannel(createPublicRequest);
  }

  // 비공개 채널 생성
  // TODO: UUID 형식이 맞으면 비공개 채널 생성이 가능한 상태, 추후 전역 처리 시 검증 로직 추가 예정
  @PostMapping("/private")
  @ResponseStatus(HttpStatus.CREATED)
  public ChannelDto.Response createPrivateChannel(
      @RequestBody ChannelDto.CreatePrivateRequest createPrivateRequest) {
    return channelService.createPrivateChannel(createPrivateRequest);
  }

  // 채널 정보 수정
  @PatchMapping("/{channelId}")
  public ChannelDto.Response update(
      @PathVariable UUID channelId,
      @RequestBody ChannelDto.UpdateRequest request) {
    return channelService.update(channelId, request);
  }

  @DeleteMapping("/{channelId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);
  }

  @GetMapping
  public List<ChannelDto.Response> getChannelsByUserId(@RequestParam UUID userId) {
    return channelService.findAllByUserId(userId);
  }
  // TODO: 채널 입장과 퇴장 로직 구현 예졍
}
