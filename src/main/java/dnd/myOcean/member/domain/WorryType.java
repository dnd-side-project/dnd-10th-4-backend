package dnd.myOcean.member.domain;

import dnd.myOcean.member.exception.WorryTypeContainsNotAccepted;

public enum WorryType {

    WORK, COURSE, RELATIONSHIP, BREAK_LOVE,
    LOVE, STUDY, FAMILY, ETC;

    public static WorryType from(final String value) {
        for (WorryType worry : WorryType.values()) {
            if (worry.name().equalsIgnoreCase(value)) {
                return worry;
            }
        }
        throw new WorryTypeContainsNotAccepted();
    }
}
