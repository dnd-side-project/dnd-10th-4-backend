package dnd.myOcean.domain.member;

import dnd.myOcean.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Worry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "worry_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private WorryType worryType;

    public static Worry createWorry(WorryType worryType) {
        Worry worry = new Worry();
        worry.initWorryType(worryType);
        return worry;
    }

    private void initWorryType(WorryType worryType) {
        this.worryType = worryType;
    }
}
