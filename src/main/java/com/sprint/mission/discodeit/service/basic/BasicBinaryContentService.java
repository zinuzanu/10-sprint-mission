package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.BusinessException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @Transactional
  @Override
  public BinaryContentDto.Response create(BinaryContentDto.CreateRequest request) {
    BinaryContent binaryContent = new BinaryContent(
        request.fileName(),
        request.size(),
        request.contentType(),
        request.bytes()
    );
    return convertToResponse(binaryContentRepository.save(binaryContent));
  }

  @Override
  public BinaryContentDto.Response findById(UUID id) {
    return convertToResponse(findBinaryContentById(id));
  }

  @Override
  public BinaryContent findEntityById(UUID id) {
    return findBinaryContentById(id);
  }

  @Override
  public List<BinaryContentDto.Response> findAllIdIn(List<UUID> ids) {
    return binaryContentRepository.findAllById(ids).stream()
        .map(this::convertToResponse)
        .toList();
  }

  @Transactional
  @Override
  public void delete(UUID id) {
    BinaryContent content = findBinaryContentById(id);
    binaryContentRepository.delete(content);
  }

  // [헬퍼 메서드]: 반복되는 조회 및 예외 처리 공통화
  private BinaryContent findBinaryContentById(UUID id) {
    return binaryContentRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ErrorCode.BINARY_CONTENT_NOT_FOUND));
  }

  private BinaryContentDto.Response convertToResponse(BinaryContent binaryContent) {
    return new BinaryContentDto.Response(
        binaryContent.getId(),
        binaryContent.getCreatedAt(),
        binaryContent.getFileName(),
        binaryContent.getSize(),
        binaryContent.getContentType(),
        binaryContent.getBytes()
    );
  }
}
