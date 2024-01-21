package dnd.myOcean.domain.member;

import dnd.myOcean.domain.base.BaseEntity;
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
    private String name;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String provider;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Member update(String name, String provider) {
        this.name = name;
        this.provider = provider;
        return this;
    }

    public String getRoleKey() {
        return this.role.getAuthority();
    }
}
