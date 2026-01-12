package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

public class JCFUserService extends JCFBaseService<User> implements UserService {

    // 이메일 중복 시 예외를 던져 가입 중단 (Fail-Fast)
    @Override
    public void create(User user) {
        validateDuplicateEmail(user.getUserEmail());
        super.create(user);
    }


    // 이메일 중복 시 예외를 던져 가입 중단 (Fail-Fast)
    private void validateDuplicateEmail(String email) {

        boolean exists = data.values().stream()
                .anyMatch(existingUser ->
                        existingUser.getUserEmail()
                                .equals(email));
        if (exists) {
            throw new IllegalStateException("이미 존재하는 이메일입니다: " + email);
        }
    }
}
