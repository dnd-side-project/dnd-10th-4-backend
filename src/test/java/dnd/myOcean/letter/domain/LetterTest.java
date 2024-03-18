package dnd.myOcean.letter.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dnd.myOcean.member.domain.Member;
import dnd.myOcean.member.domain.WorryType;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LetterTest {

    private Member sender;
    private Member receiver;

    @BeforeEach
    void init() throws Exception {
        Field idField = Member.class.getDeclaredField("id");
        idField.setAccessible(true);

        sender = Member.createFirstLoginMember("sender@mail.com");
        idField.set(sender, 1L);
        receiver = Member.createFirstLoginMember("receiver@mail.com");
        idField.set(receiver, 2L);
    }

    @DisplayName("편지를 생성한다.")
    @Test
    void testCreateLetter() {
        // when
        LetterTag letterTag = LetterTag.of(0, 10, true);
        Letter letter = Letter.createLetter(sender,
                "편지 내용",
                receiver,
                WorryType.LOVE,
                letterTag,
                null,
                "123");

        // then
        assertNotNull(letter);
        assertEquals("편지 내용", letter.getContent());
        assertFalse(letter.isDeleteBySender());
        assertFalse(letter.isHasReplied());
        assertFalse(letter.isStored());
        assertEquals(sender, letter.getSender());
        assertEquals(receiver, letter.getReceiver());
        assertEquals(WorryType.LOVE, letter.getWorryType());
        assertEquals(letterTag, letter.getLetterTag());
        assertNull(letter.getSendletterImage());
        assertEquals("123", letter.getUuid());
    }

    @DisplayName("편지에 대한 답장을 생성한다.")
    @Test
    void testReply() {
        // given
        Letter letter = new Letter();
        String replyContent = "답장 내용";

        // when
        letter.reply(replyContent, null);

        // then
        assertTrue(letter.isHasReplied());
        assertEquals(replyContent, letter.getReplyContent());
        assertNull(letter.getSendletterImage());
        assertNotNull(letter.getRepliedDate());
    }

    @DisplayName("편지를 보관하는 경우 true를 반환한다.")
    @Test
    void testStore() {
        // given
        Letter letter = new Letter();
        assertFalse(letter.isStored());

        // when
        letter.store(true);

        // then
        assertTrue(letter.isStored());
    }

    @DisplayName("편지를 받는 사람을 업데이트한다.")
    @Test
    void testUpdateReceiver() {
        // given
        Letter letter = new Letter();
        assertNull(letter.getReceiver());

        // when
        letter.updateReceiver(receiver);

        // then
        assertEquals(receiver, letter.getReceiver());
    }
}
