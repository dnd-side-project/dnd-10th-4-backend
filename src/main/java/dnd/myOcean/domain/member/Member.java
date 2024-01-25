package dnd.myOcean.domain.member;

import dnd.myOcean.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, updatable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private String nickName;

    @Column
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ElementCollection(targetClass = Worry.class) // JPA가 해당 필드를 컬렉션으로 관리하기 위한 어노테이션
    @Enumerated(EnumType.STRING)
    private List<Worry> worry;

    @Column
    private Integer updatedAge;

    @Column
    private Integer updatedGender;

    @Column
    private Integer updateNickname;

    @Column
    private Integer updateWorry;

    public void updateAge(final String birthday) {
        LocalDate birthDay = LocalDate.parse(birthday);
        LocalDate now = LocalDate.now();
        this.age = calculateAge(birthDay, now);
        this.updatedAge++;
    }

    public void updateGender(final Gender gender) {
        this.gender = gender;
        this.updatedGender++;
    }

    public void updateNickname(final String nickname) {
        this.nickName = nickname;
        this.updateNickname++;
    }

    public void updateWorry(final List<Worry> worries) {
        this.worry = worries;
        this.updateWorry = worries.size();
    }

    public boolean isBirthDayChangeLimitExceeded() {
        return updatedAge >= 2;
    }

    public boolean isGenderChangeLimitExceeded() {
        return updatedGender >= 2;
    }

    public boolean isNicknameChangeLimitExceeded() {
        return updateNickname >= 2;
    }

    private static Integer calculateAge(LocalDate birthday, LocalDate currentDate) {
        Period period = Period.between(birthday, currentDate);
        int age = period.getYears();
        if (currentDate.isBefore(birthday.plusYears(age))) {
            age--;
        }
        return age;
    }
}
