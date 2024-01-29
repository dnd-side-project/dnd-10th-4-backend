package dnd.myOcean.domain.member;

import dnd.myOcean.exception.member.WorrySelectionRangeLimitException;

public enum WorryType {

    WORK, COURSE, RELATIONSHIP, BREAK_LOVE,
    LOVE, STUDY, FAMILY, ETC;

    public static WorryType from(String value) {
        for (WorryType worry : WorryType.values()) {
            if (worry.name().equalsIgnoreCase(value)) {
                return worry;
            }
        }
        throw new WorrySelectionRangeLimitException();
    }
}
