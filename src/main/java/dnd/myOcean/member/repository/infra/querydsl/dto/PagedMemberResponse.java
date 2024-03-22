package dnd.myOcean.member.repository.infra.querydsl.dto;

import dnd.myOcean.member.domain.dto.response.MemberInfoResponse;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PagedMemberResponse {

    private Long totalElements;
    private Integer totalPage;
    private boolean hasNextPage;
    private List<MemberInfoResponse> letters;

    public static PagedMemberResponse of(Page<MemberInfoResponse> page) {
        return new PagedMemberResponse(page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.getContent());
    }
}
