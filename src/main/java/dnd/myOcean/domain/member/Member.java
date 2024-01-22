package dnd.myOcean.domain.member;

import dnd.myOcean.domain.base.BaseEntity;
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
    private String birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private boolean updatedBirthday;

    @Column
    private boolean updatedGender;

    public void updateBirthday(final String birthday) {
        this.birthday = birthday;
        this.updatedBirthday = true;
    }

    public void updateGender(final Gender gender, final boolean updated) {
        this.gender = gender;
        this.updatedGender = updated;
    }

    public boolean isAlreadyUpdateBirthday() {
        return updatedBirthday;
    }

    public boolean isAlreadyUpdateGender() {
        return updatedGender;
    }

    public boolean isGenderEqualToRequest(final Gender gender) {
        return this.gender.equals(gender);
    }
}
