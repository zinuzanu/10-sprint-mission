package com.sprint.mission.discodeit.entity;

public class Channel extends BaseEntity {
    private String name;

    public Channel(String name) {
        super();
        this.name = name;
    }

    public void updateChannel(String name) {
        this.name = name;
        super.update();
    }

    @Override
    public String toString() {
        return String.format("Channel[이름: %s, ID: %s]", name, getId());
    }

    public String getName() {
        return name;
    }
}
