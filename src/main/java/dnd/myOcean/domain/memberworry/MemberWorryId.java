package dnd.myOcean.domain.memberworry;

import dnd.myOcean.domain.member.Member;
import dnd.myOcean.domain.worry.Worry;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberWorryId implements Serializable {

    private Member member;
    private Worry worry;
}
