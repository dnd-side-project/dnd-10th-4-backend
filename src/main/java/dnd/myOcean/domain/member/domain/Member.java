package dnd.myOcean.domain.member.domain;

import dnd.myOcean.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public void setWorries(List<Worry> worries) {
        this.worries.clear();

        List<MemberWorry> memberWorries = worries.stream()
                .map(worry -> new MemberWorry(this, worry))
                .collect(Collectors.toList());

        this.worries.addAll(memberWorries);
    }

    public void clearWorries() {
        this.worries.clear();
    }
}
