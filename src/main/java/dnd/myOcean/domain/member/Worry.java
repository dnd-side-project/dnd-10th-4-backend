package dnd.myOcean.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Worry {

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
