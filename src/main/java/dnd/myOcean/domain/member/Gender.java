package dnd.myOcean.domain.member;

import dnd.myOcean.exception.member.NoSuchGenderException;
import lombok.Getter;

@Getter
public enum Gender {
    
    MALE, FEMALE, NONE;

    public static Gender from(String value) {
        for (Gender gender : Gender.values()) {
            if (gender.name().equalsIgnoreCase(value)) {
                return gender;
            }
        }
        throw new NoSuchGenderException();
    }
}

