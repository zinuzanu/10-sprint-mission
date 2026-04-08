package com.sprint.mission.discodeit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class UserIntegrationTest extends BaseIntegrationTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("사용자 통합 시나리오: 새로운 사용자를 등록하고 목록에서 정상적으로 조회할 수 있어야 합니다")
  void user_create_and_list_success() throws Exception {
    // given
    UserCreateRequest request = new UserCreateRequest("test", "test@email.com", "test1234");
    MockMultipartFile requestPart = new MockMultipartFile("userCreateRequest", "",
        MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request));

    // when
    mockMvc.perform(multipart("/api/users").file(requestPart))
        .andExpect(status().isCreated());

    // then
    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].username").value("test"))
        .andExpect(jsonPath("$[0].email").value("test@email.com"));
  }

  @Test
  @DisplayName("사용자 가입 예외: 중복된 이메일로 가입을 시도할 경우 400 Bad Request 응답과 함께 가입이 거절되어야 합니다.")
  void user_create_duplicate_email_fail() throws Exception {
    // given
    userRepository.save(new User("test1", "test@email.com", "test1234"));

    // when
    UserCreateRequest request = new UserCreateRequest("test2", "test@email.com", "test4321");
    MockMultipartFile requestPart = new MockMultipartFile("userCreateRequest", "",
        MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request));

    // then
    mockMvc.perform(multipart("/api/users").file(requestPart))
        .andExpect(status().isBadRequest());
  }
}
