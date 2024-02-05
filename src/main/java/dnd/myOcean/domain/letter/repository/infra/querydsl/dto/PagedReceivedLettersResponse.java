package dnd.myOcean.domain.letter.repository.infra.querydsl.dto;

import dnd.myOcean.domain.letter.domain.dto.response.ReceivedLetterResponse;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PagedReceivedLettersResponse {

    private Long totalElements;
    private Integer totalPage;
    private boolean hasNextPage;
    private List<ReceivedLetterResponse> postList;

    public static PagedReceivedLettersResponse of(Page<ReceivedLetterResponse> page) {
        return new PagedReceivedLettersResponse(
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.getContent());
    }
}
