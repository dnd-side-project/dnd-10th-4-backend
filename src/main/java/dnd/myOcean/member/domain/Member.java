package dnd.myOcean.member.domain;

import dnd.myOcean.global.common.base.BaseEntity;
import dnd.myOcean.member.domain.dto.request.InfoUpdateRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
    private LocalDate birthDay;

    @Column
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MemberWorry> worries = new ArrayList<>();

    @Column
    private Integer updateAgeCount;

    @Column
    private Integer updateGenderCount;

    @Column
    private Integer letterCount;

    public void updateBirthday(final String birthday) {
        LocalDate birthDay = LocalDate.parse(birthday);
        this.birthDay = birthDay;
        updateAge(birthDay);
    }

    private void updateAge(final LocalDate birthDay) {
        LocalDate now = LocalDate.now();
        this.age = calculateAge(birthDay, now);
        this.updateAgeCount++;
    }

    private static Integer calculateAge(final LocalDate birthday, final LocalDate currentDate) {
        Period period = Period.between(birthday, currentDate);
        int age = period.getYears();
        if (currentDate.isBefore(birthday.plusYears(age))) {
            age--;
        }
        return age;
    }

    public void updateGender(final Gender gender) {
        this.gender = gender;
        this.updateGenderCount++;
    }

    public void updateNickname(final String nickname) {
        this.nickName = String.format("낯선 %s %d", nickname, this.id);
    }

    public boolean isBirthDayChangeLimitExceeded() {
        return updateAgeCount >= 2;
    }

    public boolean isGenderChangeLimitExceeded() {
        return updateGenderCount >= 2;
    }

    public void updateWorries(final List<Worry> worries) {
        this.worries.clear();
        List<MemberWorry> memberWorries = worries.stream()
                .map(worry -> new MemberWorry(this, worry))
                .collect(Collectors.toList());
        this.worries.addAll(memberWorries);
    }

    public void clearWorries() {
        this.worries.clear();
    }

    public boolean isFirstLogin() {
        return nickName == null &&
                gender.equals(Gender.NONE) &&
                updateAgeCount.equals(0) &&
                updateGenderCount.equals(0);
    }

    public static Member createFirstLoginMember(final String email) {
        return Member.builder()
                .email(email)
                .role(Role.USER)
                .gender(Gender.NONE)
                .updateAgeCount(0)
                .updateGenderCount(0)
                .letterCount(5)
                .build();
    }

    public void updateInfo(final InfoUpdateRequest request, final List<Worry> worries) {
        updateNickname(request.getNickname());
        updateBirthday(request.getBirthday());
        updateGender(Gender.from(request.getGender()));
        updateWorries(worries);
    }

    public boolean exceedLetterLimit() {
        return this.letterCount <= 0;
    }

    public void resetLetterCount(int letterCount) {
        this.letterCount = letterCount;
    }

    public void reduceLetterCount() {
        this.letterCount--;
    }
}
