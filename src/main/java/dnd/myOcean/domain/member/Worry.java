package dnd.myOcean.domain.member;

import dnd.myOcean.exception.member.NoSuchGenderException;

public enum Worry {
    WORK, COURSE, RELATIONSHIP, BREAK_LOVE,
    LOVE, STUDY, FAMILY, ETC;

    public static Worry from(String value) {
        for (Worry worry : Worry.values()) {
            if (worry.name().equalsIgnoreCase(value)) {
                return worry;
            }
        }
        throw new NoSuchGenderException();
    }
}
