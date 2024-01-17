package dnd.myOcean.domain.member;

import dnd.myOcean.domain.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

    @Column
    private String username;

    @Column(nullable = false)
    private String nickname;
}

