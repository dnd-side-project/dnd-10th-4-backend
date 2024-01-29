package dnd.myOcean.domain.member;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class MemberWorryId implements Serializable {

    private Member member;

    private Worry worry;
}
