package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  List<ReadStatus> findAllByUserId(UUID userId);

  @Query("""
      SELECT rs
      FROM ReadStatus rs
      JOIN FETCH rs.user
      WHERE rs.channel.id = :channelId
      """)
  List<ReadStatus> findAllByChannelId(UUID channelId);

  Optional<ReadStatus> findByChannelIdAndUserId(UUID channelId, UUID userId);

  void deleteByUser(User user);

  void deleteByChannel(Channel channel);
}
