package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

public class JCFMessageService extends JCFBaseService<Message> implements MessageService {
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public void create(Message entity) {
        super.create(entity);
    }

    // 메세지 저장 시 참조 무결성 확인 (Fail-Fast)
    // 작성자 및 채널 존재 여부 확인
    private void validateReference(UUID userId, UUID channelId) {
        if (userService.findById(userId) == null)
            throw new IllegalArgumentException("존재하지 않는 유저입니다. (ID: " + userId + ")");
        if (channelService.findById(channelId) == null)
            throw new IllegalArgumentException("존재하지 않는 채널입니다. (ID: " + channelId + ")");
    }
}
