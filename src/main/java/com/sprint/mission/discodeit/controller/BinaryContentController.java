package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "3. 리소스 관리", description = "파일(바이너리 데이터) 조회 및 관리 API")
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  @Operation(summary = "단일 파일 정보 조회", description = "ID를 통해 특정 파일의 상세 정보와 바이너리 데이터를 조회합니다.")
  @GetMapping("/{binaryContentId}")
  public BinaryContentDto find(
      @Parameter(description = "조회할 파일의 UUID", required = true)
      @PathVariable UUID binaryContentId) {
    return binaryContentService.findById(binaryContentId);
  }

  @Operation(summary = "파일 목록 다중 조회", description = "여러 개의 ID를 파라미터로 받아 해당하는 파일 정보들을 한꺼번에 조회합니다.")
  @GetMapping
  public List<BinaryContentDto> findAllByIdIn(
      @Parameter(description = "조회할 파일 UUID 리스트", required = true)
      @RequestParam List<UUID> binaryContentIds) {
    return binaryContentService.findAllByIdIn(binaryContentIds);
  }

  @Operation(summary = "파일 다운로드", description = "ID를 통해 실제 바이너리 파일을 다운로드합니다.")
  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<?> download(
      @Parameter(description = "다운로드할 파일의 UUID", required = true)
      @PathVariable("binaryContentId") UUID binaryContentId) {
    BinaryContentDto dto = binaryContentService.findById(binaryContentId);
    return binaryContentStorage.download(dto);
  }
}
