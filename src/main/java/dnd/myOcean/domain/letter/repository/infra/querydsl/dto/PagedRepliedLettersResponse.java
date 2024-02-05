package dnd.myOcean.domain.letter.repository.infra.querydsl.dto;

import dnd.myOcean.domain.letter.domain.dto.response.RepliedLetterResponse;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PagedRepliedLettersResponse {

    private Long totalElements;
    private Integer totalPage;
    private boolean hasNextPage;
    private List<RepliedLetterResponse> postList;

    public static PagedRepliedLettersResponse of(Page<RepliedLetterResponse> page) {
        return new PagedRepliedLettersResponse(
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.getContent());
    }
}
