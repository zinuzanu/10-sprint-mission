package com.sprint.mission.discodeit.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 필드가 포함된 Multipart 요청 처리 시, Swagger UI 등에서 발생하는 Content-Type(application/octet-stream) 불일치 문제를
 * 해결하기 위한 설정 클래스입니다.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  private final MDCLoggingInterceptor mdcLoggingInterceptor;

  public WebMvcConfig(MappingJackson2HttpMessageConverter converter,
      MDCLoggingInterceptor mdcLoggingInterceptor) {
    this.mdcLoggingInterceptor = mdcLoggingInterceptor;

    List<MediaType> supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());

    supportedMediaTypes.add(new MediaType("application", "octet-stream"));

    converter.setSupportedMediaTypes(supportedMediaTypes);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(mdcLoggingInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns("/css/**", "/js/**", "/images/**");
  }
}