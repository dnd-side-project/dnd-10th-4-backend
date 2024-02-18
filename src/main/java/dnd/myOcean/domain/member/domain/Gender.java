package dnd.myOcean.domain.member.domain;

import dnd.myOcean.domain.member.exception.NoSuchGenderException;
import lombok.Getter;

@Getter
public enum Gender {

    MALE, FEMALE;

    public static Gender from(String value) {
        for (Gender gender : Gender.values()) {
            if (gender.name().equalsIgnoreCase(value)) {
                return gender;
            }
        }
        throw new NoSuchGenderException();
    }
}

