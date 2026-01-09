package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaApplication {
    public static void main(String[] args) {
        // 각 도메인 클래스 인스턴스화
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService();

        // [유저 테스트 시작] 유저 생성 및 저장(Create)
        User user1 = new User("김진우", "wsx104301@naver.com");
        userService.create(user1);

        User user2 = new User("김호야", "hoya@naver.com");
        userService.create(user2);

        User user3 = new User("김꼬야", "kkoya@naver.com");
        userService.create(user3);

        // 1번 유저 Id 조회, 전체 유저 Id 조회 (Read)
        System.out.println("단건 조회: " + userService.findById(user1.getId()));
        System.out.println("다건 조회: " + userService.findAll());

        // 1번 유저 정보 수정 (Update)
        user1.updateUser("김진짜누", "realzinu@gmail.com");
        userService.update(user1.getId(), user1);
        System.out.println("수정 후 정보: " + user1);

        // 1번 유저 삭제 (Delete)
        userService.delete(user1.getId());
        System.out.println("다건 조회: " + userService.findAll());

        // [유저 테스트 종료]
        System.out.println("=".repeat(100));


        // [채널 테스트 시작] 채널 생성 및 저장(Create)
        Channel channel1 = new Channel("김진우의 채널");
        channelService.create(channel1);

        Channel channel2 = new Channel("김호야의 채널");
        channelService.create(channel2);

        // 2번 채널 Id 조회, 전체 채팅방 Id 조회 (Read)
        System.out.println("단건 조회: " + channelService.findById(channel2.getId()));
        System.out.println("다건 조회: " + channelService.findAll());

        // 1번 채널 정보 수정 (Update)
        channel1.updateChannel("김꼬야의 채널");
        channelService.update(channel1.getId(), channel1);
        System.out.println("수정 후 정보: " + channel1);

        // 1번 채널 삭제 (Delete)
        channelService.delete(channel1.getId());
        System.out.println("다건 조회: " + channelService.findAll());

        // [채널 테스트 종료]
        System.out.println("=".repeat(100));

        // [메시지 테스트 시작] 메시지 생성 및 저장 (Create)
        Message message1 = new Message(user2.getId(), channel2.getId(), "나는 김호야다! 날 키워라!");
        messageService.create(message1);

        Message message2 = new Message(user3.getId(), channel2.getId(), "나는 김꼬야다! 고개를 조아려라!");
        messageService.create(message2);

        // 메시지 조회 (Read)
        System.out.println("채널 내 메시지 목록: " + messageService.findAll());
        System.out.println("메시지 단건 조회: " + messageService.findById(message1.getId()));

        // 메시지 내용 수정 (Update)
        message1.updateMessage("냥냥냥냥냥~");
        messageService.update(message1.getId(), message1);
        System.out.println("수정된 메시지: " + messageService.findById(message1.getId()));

        // 메시지 삭제 (Delete)
        messageService.delete(message2.getId());
        System.out.println("삭제 후 메시지 목록: " + messageService.findAll());

        // [메시지 테스트 종료]
        System.out.println("=".repeat(100));
    }
}
