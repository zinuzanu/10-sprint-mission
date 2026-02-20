package com.sprint.mission.discodeit.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {


  // 1. 사용자 및 인증 그룹: 회원가입, 정보 수정 및 로그인 관련 API
  @Bean
  public GroupedOpenApi userGroup() {
    return GroupedOpenApi.builder()
        .group("1. 사용자 및 인증")
        .pathsToMatch("/api/users/**", "/api/auth/**")
        .build();
  }

  // 2. 채팅 관리 그룹: 채널, 메세지, 읽음 상태 등 채팅 비즈니스 로직 관련 API
  @Bean
  public GroupedOpenApi chatGroup() {
    return GroupedOpenApi.builder()
        .group("2. 채팅 관리")
        .pathsToMatch("/api/channels/**", "/api/messages/**", "/api/read-statuses/**")
        .build();
  }

  // 3. 리소스 관리 그룹: 이미지, 파일 업로드 등 바이너리 컨텐츠 관련 API
  @Bean
  public GroupedOpenApi resourceGroup() {
    return GroupedOpenApi.builder()
        .group("3. 리소스 관리") // 파일(바이너리) 전용 그룹
        .pathsToMatch("/api/binary-contents/**")
        .build();
  }
}
