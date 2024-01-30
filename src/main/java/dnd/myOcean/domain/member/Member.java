package dnd.myOcean.domain.member;

import dnd.myOcean.domain.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.Period;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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

    @Column
    private Integer updateAgeCount;

    @Column
    private Integer updateGenderCount;

    public void updateAge(final String birthday) {
        LocalDate birthDay = LocalDate.parse(birthday);
        LocalDate now = LocalDate.now();
        this.age = calculateAge(birthDay, now);
        this.updateAgeCount++;
    }

    public void updateGender(final Gender gender) {
        this.gender = gender;
        this.updateGenderCount++;
    }

    public void updateNickname(final String nickname) {
        this.nickName = "낯선 " + nickname;
    }

    public boolean isBirthDayChangeLimitExceeded() {
        return updateAgeCount >= 2;
    }

    public boolean isGenderChangeLimitExceeded() {
        return updateGenderCount >= 2;
    }

    private static Integer calculateAge(LocalDate birthday, LocalDate currentDate) {
        Period period = Period.between(birthday, currentDate);
        int age = period.getYears();
        if (currentDate.isBefore(birthday.plusYears(age))) {
            age--;
        }
        return age;
    }

    public boolean isNicknameEqualTo(String nickname) {
        return this.nickName.equals(nickname);
    }
}
