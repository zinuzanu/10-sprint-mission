package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public abstract class BaseEntity {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    public BaseEntity() { // 필드 초기화
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    } // ID Getter

    public Long getCreatedAt() {
        return createdAt;
    } // CreatedAt Getter

    public Long getUpdatedAt() {
        return updatedAt;
    } // UpdatedAt Getter

    protected void update() {  // 필드 수정 메서드
        this.updatedAt = System.currentTimeMillis();
    }
}
