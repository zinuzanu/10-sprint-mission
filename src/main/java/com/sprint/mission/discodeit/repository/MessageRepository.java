package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, UUID> {

  List<Message> findByChannel(Channel channel);

  Slice<Message> findSliceByChannel(Channel channel, Pageable pageable);

  Optional<Instant> findLatestMessageTimeByChannelId(UUID channelId);

  void deleteByChannel(Channel channel);
}
