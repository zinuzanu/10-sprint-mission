package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class PageResponseMapper {

  public <T, ID> PageResponse<T> toCursorPageResponse(
      List<T> content, int requestedSize, Function<T, ID> idExtractor, Long totalElements) {

    // 1. 다음 페이지 존재 여부 확인
    boolean hasNext = content.size() > requestedSize;

    // 2. 실제 전달할 데이터만 남기기 (size+1개 중 size개만)
    List<T> finalContent = hasNext ? content.subList(0, requestedSize) : content;

    // 3. 마지막 데이터에서 커서(ID) 추출
    ID nextCursor = (hasNext && !finalContent.isEmpty())
        ? idExtractor.apply(finalContent.get(finalContent.size() - 1))
        : null;

    return new PageResponse<>(finalContent, nextCursor, requestedSize, hasNext, totalElements);
  }

  public <T> PageResponse<T> fromSlice(Slice<T> slice, Function<T, ?> cursorExtractor) {
    List<T> content = slice.getContent();

    Object nextCursor = (slice.hasNext() && !content.isEmpty())
        ? cursorExtractor.apply(content.get(content.size() - 1))
        : null;

    return new PageResponse<>(
        content,
        nextCursor,
        slice.getSize(),
        slice.hasNext(),
        null
    );
  }

  public <T> PageResponse<T> fromPage(Page<T> page, Function<T, ?> cursorExtractor) {
    List<T> content = page.getContent();

    Object nextCursor = (page.hasNext() && !content.isEmpty())
        ? cursorExtractor.apply(content.get(content.size() - 1))
        : null;

    return new PageResponse<>(
        content,
        nextCursor,
        page.getSize(),
        page.hasNext(),
        page.getTotalElements()
    );
  }
}
