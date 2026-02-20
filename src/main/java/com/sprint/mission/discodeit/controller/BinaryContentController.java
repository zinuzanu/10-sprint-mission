package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binary-contents")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @GetMapping("/{binaryContentId}")
  public BinaryContentDto.Response find(@PathVariable UUID binaryContentId) {
    return binaryContentService.findById(binaryContentId);
  }

  @GetMapping
  public List<BinaryContentDto.Response> findAllByIdIn(@RequestParam List<UUID> binaryContentIds) {
    return binaryContentService.findAllIdIn(binaryContentIds);
  }
}
