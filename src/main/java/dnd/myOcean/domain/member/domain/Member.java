package dnd.myOcean.domain.member.domain;

import dnd.myOcean.domain.member.domain.dto.request.InfoUpdateRequest;
import dnd.myOcean.global.common.base.BaseEntity;
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

    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MemberWorry> worries = new ArrayList<>();

    @Column
    private Integer updateAgeCount;

    @Column
    private Integer updateGenderCount;

    public void updateBirthday(final String birthday) {
        LocalDate birthDay = LocalDate.parse(birthday);
        this.birthDay = birthDay;
        updateAge(birthDay);
    }

    private void updateAge(LocalDate birthDay) {
        LocalDate now = LocalDate.now();
        this.age = calculateAge(birthDay, now);
        this.updateAgeCount++;
    }

    private static Integer calculateAge(LocalDate birthday, LocalDate currentDate) {
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
        this.nickName = "낯선 " + nickname + " " + this.id;
    }

    public boolean isBirthDayChangeLimitExceeded() {
        return updateAgeCount >= 2;
    }

    public boolean isGenderChangeLimitExceeded() {
        return updateGenderCount >= 2;
    }

    public void updateWorries(List<Worry> worries) {
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
        return nickName.equals("NONE") &&
                gender.equals(Gender.NONE) &&
                updateAgeCount.equals(0) &&
                updateGenderCount.equals(0);
    }

    public static Member createFirstLoginMember(String email) {
        return Member.builder()
                .email(email)
                .role(Role.USER)
                .nickName("NONE")
                .gender(Gender.NONE)
                .updateAgeCount(0)
                .updateGenderCount(0)
                .build();
    }

    public void updateInfo(InfoUpdateRequest request, List<Worry> worries) {
        updateNickname(request.getNickname());
        updateBirthday(request.getBirthday());
        updateGender(Gender.from(request.getGender()));
        updateWorries(worries);
    }
}
