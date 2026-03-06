package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.BusinessException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;

  @Transactional
  @Override
  public BinaryContentDto create(BinaryContentCreateRequest request) {
    BinaryContent binaryContent = binaryContentMapper.toEntity(request);
    return binaryContentMapper.toDto(binaryContentRepository.save(binaryContent));
  }

  @Override
  public BinaryContentDto findById(UUID id) {
    return binaryContentMapper.toDto(findBinaryContentById(id));
  }

  @Override
  public BinaryContent findEntityById(UUID id) {
    return findBinaryContentById(id);
  }

  @Override
  public List<BinaryContentDto> findAllIdIn(List<UUID> ids) {
    return binaryContentRepository.findAllById(ids).stream()
        .map(binaryContentMapper::toDto)
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
}
