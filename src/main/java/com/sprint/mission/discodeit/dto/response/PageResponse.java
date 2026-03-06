package com.sprint.mission.discodeit.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "공통 페이지네이션 응답")
public class PageResponse<T> {

  @Schema(description = "실제 데이터 리스트")
  private List<T> content;

  @Schema(description = "현재 페이지 번호")
  private int number;

  @Schema(description = "페이지 크기")
  private int size;

  @Schema(description = "다음 페이지 존재 여부")
  private boolean hasNext;

  @Schema(description = "전체 데이터 개수 (Slice 조회 시 null)", nullable = true)
  private Long totalElements;
}
