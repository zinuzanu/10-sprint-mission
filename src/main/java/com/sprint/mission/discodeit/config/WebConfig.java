package com.sprint.mission.discodeit.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 필드가 포함된 Multipart 요청 처리 시, Swagger UI 등에서 발생하는 Content-Type(application/octet-stream) 불일치 문제를
 * 해결하기 위한 설정 클래스입니다.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

  public WebConfig(MappingJackson2HttpMessageConverter converter) {
// 1. 기존 Jackson 메시지 컨버터가 지원하는 미디어 타입 리스트를 가져옵니다.
    List<MediaType> supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());

    // 2. Swagger UI가 파일을 보내지 않을 때 JSON 파트에 설정하는 'application/octet-stream'을 허용 리스트에 추가합니다.
    // 이를 통해 스프링이 해당 타입을 만나도 당황하지 않고 JSON으로 변환(Mapping)할 수 있게 합니다.
    supportedMediaTypes.add(new MediaType("application", "octet-stream"));
    
    // 3. 변경된 리스트를 다시 컨버터에 설정합니다.
    converter.setSupportedMediaTypes(supportedMediaTypes);
  }
}