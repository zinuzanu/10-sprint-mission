package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity {
    private String content;
    private final UUID userId;
    private final UUID channelId;

    public Message(UUID userId, UUID channelId, String content) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.content = content;
    }

    public void updateMessage(String content) {
        this.content = content;
        super.update();
    }

    @Override
    public String toString() {
        return String.format("Message[내용: %s, 작성자: %s, 채널: %s, ID: %s]",
                content, userId, channelId, getId());
    }

    public String getContent() {
        return content;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChannelId() {
        return channelId;
    }
}
