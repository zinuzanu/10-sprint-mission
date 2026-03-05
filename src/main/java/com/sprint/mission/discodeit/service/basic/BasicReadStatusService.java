package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.ReadStatusDto.UpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.BusinessException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;

  @Override
  public ReadStatusDto.Response create(ReadStatusDto.CreateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(() -> new BusinessException(ErrorCode.CHANNEL_NOT_FOUND));

    boolean isDuplicate = readStatusRepository.findAll().stream()
        .anyMatch(rs -> rs.getUser().getId().equals(request.userId())
            && rs.getChannel().getId().equals(request.channelId()));
    if (isDuplicate) {
      throw new BusinessException(ErrorCode.READ_STATUS_ALREADY_EXISTS);
    }

    ReadStatus readStatus = new ReadStatus(
        user,
        channel
    );

    return convertToResponse(readStatusRepository.save(readStatus));
  }

  @Override
  public ReadStatusDto.Response findById(UUID id) {
    return convertToResponse(findReadStatusEntityById(id));
  }

  @Override
  public List<ReadStatusDto.Response> findAllByUserId(UUID userId) {
    return readStatusRepository.findAll().stream()
        .filter(rs -> rs.getUser().getId().equals(userId))
        .map(this::convertToResponse)
        .toList();
  }

  @Override
  public ReadStatusDto.Response update(UUID readStatusId, UpdateRequest request) {
    ReadStatus readStatus = findReadStatusEntityById(readStatusId);
    readStatus.updateLastReadAt();
    return convertToResponse(readStatusRepository.save(readStatus));
  }

  @Override
  public void delete(UUID id) {
    findReadStatusEntityById(id);
    readStatusRepository.deleteById(id);
  }

  // [헬퍼 메서드]: 반복되는 조회 및 예외 처리 공통화
  private ReadStatus findReadStatusEntityById(UUID id) {
    return readStatusRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ErrorCode.READ_STATUS_NOT_FOUND));
  }

  // [헬퍼 메서드]: 엔티티를 클라이언트 응답용 DTO로 변환 및 데이터 가공
  private ReadStatusDto.Response convertToResponse(ReadStatus readStatus) {
    return new ReadStatusDto.Response(
        readStatus.getId(),
        readStatus.getUser().getId(),
        readStatus.getChannel().getId(),
        readStatus.getLastReadAt()
    );
  }
}
