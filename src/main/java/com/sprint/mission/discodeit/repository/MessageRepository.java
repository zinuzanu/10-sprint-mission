package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  Optional<Message> findFirstByChannelOrderByCreatedAtDesc(Channel channel);

  // 1. 커서가 없을 때 (첫 페이지 조회)
  List<Message> findByChannelOrderByCreatedAtDesc(Channel channel, Pageable pageable);

  // 2. 커서가 있을 때 (다음 페이지 조회)
  // 커서(UUID)에 해당하는 메시지의 생성일보다 더 과거의 메시지들을 조회합니다.
  @Query("SELECT m FROM Message m WHERE m.channel = :channel " +
      "AND m.createdAt < (SELECT m2.createdAt FROM Message m2 WHERE m2.id = :cursor) " +
      "ORDER BY m.createdAt DESC")
  List<Message> findByChannelAndCursor(@Param("channel") Channel channel,
      @Param("cursor") UUID cursor,
      Pageable pageable);

  @EntityGraph(attributePaths = {"author", "attachments"})
  Optional<Message> findWithAuthorAndAttachmentsById(UUID id);

  long countByChannel(Channel channel);

  void deleteByChannel(Channel channel);
}
