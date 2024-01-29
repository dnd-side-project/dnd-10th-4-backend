package dnd.myOcean.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberWorryId implements Serializable {

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "worry_id")
    private Long worryId;

    public MemberWorryId(Long memberId, Long worryId) {
        this.memberId = memberId;
        this.worryId = worryId;
    }
}
