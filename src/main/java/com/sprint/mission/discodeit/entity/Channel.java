package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.exception.channel.InvalidChannelNameException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "channels")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel extends BaseUpdatableEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private ChannelType type;

  @Column(name = "name", length = 100)
  private String name;

  @Column(name = "description", length = 500)
  private String description;

  @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ReadStatus> readStatuses = new ArrayList<>();

  public void addReadStatus(ReadStatus readStatus) {
    this.readStatuses.add(readStatus);
  }

  public List<User> getParticipants() {
    return this.readStatuses.stream().map(ReadStatus::getUser).toList();
  }

  public Channel(String name, String description, ChannelType type) {
    super();
    if (type == ChannelType.PUBLIC) {
      validateChannel(name);
    }
    this.name = name;
    this.description = description;
    this.type = type;
  }

  public void update(String newName, String newDescription) {

    if (newName != null && !newName.equals(this.name)) {
      validateChannel(newName);
      this.name = newName;
    }

    // 설명(newDescription)은 null이거나 비어있을 수도 있으니 동등 비교만 수행
    if (newDescription != null && !newDescription.equals(this.description)) {
      this.description = newDescription;
    }
  }


  // 채널 생성 및 수정 시 준수해야 할 비즈니스 정책 (Fail-Fast)
  private void validateChannel(String channelName) {
    // null, Blank 체크
    if (channelName == null || channelName.length() < 2 || channelName.length() > 100) {
      throw new InvalidChannelNameException(channelName);
    }
  }

  @Override
  public String toString() {
    return String.format("Channel[이름: %s, Channel ID: %s]", name, getId());
  }
}