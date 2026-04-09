package com.sprint.mission.discodeit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.dto.ChannelCreatePublicRequest;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class ChannelIntegrationTest extends BaseIntegrationTest {

  @Test
  @DisplayName("공개 채널 시나리오: 새로운 공개 채널을 생성하고 목록에서 정상적으로 확인되어야 합니다.")
  void public_channel_create_and_list_success() throws Exception {
    // given
    ChannelCreatePublicRequest request = new ChannelCreatePublicRequest("Public Channel",
        "누구나 접근 가능한 공개 채널");

    // when
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());

    // then
    mockMvc.perform(get("/api/channels")
            .param("userId", UUID.randomUUID().toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Public Channel"))
        .andExpect(jsonPath("$[0].type").value("PUBLIC"));
  }

  @Test
  @DisplayName("채널 생성 예외: 이름이 2자 미만인 공개 채널을 생성할 경우 400 Bad Request를 반환해야 합니다.")
  void public_channel_create_fail_short_name() throws Exception {
    // given
    ChannelCreatePublicRequest request = new ChannelCreatePublicRequest("A", "너무 짧은 이름");

    // when, then
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }
}
