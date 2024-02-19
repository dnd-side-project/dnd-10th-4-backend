package dnd.myOcean.letter.repository.infra.querydsl.dto;

import dnd.myOcean.letter.domain.dto.response.StoredLetterResponse;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PagedStoredLetterResponse {

    private Long totalElements;
    private Integer totalPage;
    private boolean hasNextPage;
    private List<StoredLetterResponse> letters;

    public static PagedStoredLetterResponse of(Page<StoredLetterResponse> page) {
        return new PagedStoredLetterResponse(
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.getContent());
    }
}
