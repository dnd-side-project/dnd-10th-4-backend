package dnd.myOcean.member.repository.infra.querydsl.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class MemberReadCondition {

    @NotNull(message = "페이지 번호를 입력해주세요.")
    @PositiveOrZero(message = "올바른 페이지 번호를 입력해주세요. (0 이상)")
    private Integer page;

    @NotNull(message = "페이지 크기를 입력해주세요.")
    @Positive(message = "올바른 페이지 크기를 입력해주세요. (1 이상)")
    private Integer size;

    private String email;

    public MemberReadCondition() {
        this.page = getDefaultPageNum();
        this.size = getDefaultPageSize();
    }

    private int getDefaultPageNum() {
        return 0;
    }

    private int getDefaultPageSize() {
        return 10;
    }
}
