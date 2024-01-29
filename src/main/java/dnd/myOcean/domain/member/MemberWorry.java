package dnd.myOcean.domain.member;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberWorry {

    @EmbeddedId
    private MemberWorryId  id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worry_id")
    @NotNull
    private Worry worry;


    @Builder
    public MemberWorry(Member member, Worry worry) {
        this.member = member;
        this.worry = worry;
        this.id = new MemberWorryId(member.getId(), worry.getId());
    }
}
