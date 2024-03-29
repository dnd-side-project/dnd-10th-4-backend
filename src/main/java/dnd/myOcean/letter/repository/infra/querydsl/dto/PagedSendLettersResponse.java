package dnd.myOcean.letter.repository.infra.querydsl.dto;

import dnd.myOcean.letter.domain.dto.response.SendLetterResponse;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PagedSendLettersResponse {

    private Long totalElements;
    private Integer totalPage;
    private boolean hasNextPage;
    private List<SendLetterResponse> letters;

    public static PagedSendLettersResponse of(Page<SendLetterResponse> page) {
        return new PagedSendLettersResponse(
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.getContent());
    }
}
