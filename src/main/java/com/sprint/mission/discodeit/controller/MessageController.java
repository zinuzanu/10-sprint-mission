package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  // 메시지 생성
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public MessageDto.Response create(
      @RequestPart("messageCreateRequest") MessageDto.CreateRequest request,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {
    return messageService.create(request, attachments);
  }

  // 메시지 수정
  @PatchMapping("/{messageId}")
  public MessageDto.Response update(
      @PathVariable UUID messageId,
      @RequestBody MessageDto.UpdateRequest request) {
    return messageService.update(messageId, request);
  }

  // 메시지 삭제
  @DeleteMapping("/{messageId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID messageId) {
    messageService.delete(messageId);
  }

  // 특정 채널 메세지 목록 조회
  @GetMapping
  public List<MessageDto.Response> findAllByChannelId(
      @RequestParam UUID channelId) {
    return messageService.findAllByChannelId(channelId);
  }
}
