package dnd.myOcean.domain.letter.repository.infra.querydsl.dto;

import dnd.myOcean.domain.letter.dto.response.LetterResponse;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PagedLettersResponse {

    private Long totalElements;
    private Integer totalPage;
    private boolean hasNextPage;
    private List<LetterResponse> postList;

    public static PagedLettersResponse of(Page<LetterResponse> page) {
        return new PagedLettersResponse(
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.getContent());
    }
}
