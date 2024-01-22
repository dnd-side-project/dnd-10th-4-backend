package dnd.myOcean.domain.member;

import dnd.myOcean.domain.base.BaseEntity;
import dnd.myOcean.dto.member.request.MemberBirthdayRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Column
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private String nickName;

    @Column
    private String birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public void updateBirthday(MemberBirthdayRequest birthday) {
        this.birthday = birthday.getBirthday();
    }

    public void updateGender(Gender gender) {
        this.gender = gender;
    }
}
