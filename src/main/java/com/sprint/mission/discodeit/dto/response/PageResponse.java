package com.sprint.mission.discodeit.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "커서 기반 페이지네이션 응답")
public class PageResponse<T> {

  @Schema(description = "실제 데이터 리스트")
  private List<T> content;

  @Schema(description = "다음 조회를 위한 커서 식별자")
  private Object nextCursor;

  @Schema(description = "페이지 크기")
  private int size;

  @Schema(description = "다음 페이지 존재 여부")
  private boolean hasNext;

  @Schema(description = "전체 데이터 개수 (Slice 조회 시 null)", nullable = true)
  private Long totalElements;
}
