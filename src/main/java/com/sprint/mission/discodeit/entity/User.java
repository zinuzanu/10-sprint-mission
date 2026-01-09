package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String name;
    private String email;

    public User(String name, String email) {
        super();
        this.name = name;
        this.email = email;
    }

    public void updateUser(String name, String email) {
        this.name = name;
        this.email = email;
        super.update();
    }

    @Override
    public String toString() {
        return String.format("User[이름: %s, 이메일: %s, ID: %s]", name, email, getId());
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
