package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity {
    private String content;
    private final UUID userId;
    private final UUID channelId;

    public Message(UUID userId, UUID channelId, String content) {
        super();
        validateMessage();
        this.userId = userId;
        this.channelId = channelId;
        this.content = content;
    }

    public void updateMessage(String content) {
        validateMessage();
        this.content = content;
        super.update();
    }

    // 메세지 생성 및 수정 시 준수해야 할 비즈니스 정책 (Fail-Fast)
    // content.length() < 1는 항상 false를 반환하므로 작성 하지 않음
    private void validateMessage() {

        // null, Blank 체크
        if (content == null || content.isBlank()) throw new IllegalArgumentException("메세지 내용을 입력해주세요");

        // 메세지 길이 체크 (1자 이상, 500자 이하)
        if (content.length() > 500) throw new IllegalArgumentException("메세지는 1자 이상, 500자 이하로 작성해주세요.");
    }

    @Override
    public String toString() {
        return String.format("Message[내용: %s, 작성자: %s, 채널: %s, Message ID: %s]",
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
