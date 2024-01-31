package dnd.myOcean.domain.member.domain;

import dnd.myOcean.domain.member.exception.exceptions.WorryTypeContainsNotAccepted;

public enum WorryType {

    WORK, COURSE, RELATIONSHIP, BREAK_LOVE,
    LOVE, STUDY, FAMILY, ETC;

    public static WorryType from(String value) {
        for (WorryType worry : WorryType.values()) {
            if (worry.name().equalsIgnoreCase(value)) {
                return worry;
            }
        }
        throw new WorryTypeContainsNotAccepted();
    }
}
