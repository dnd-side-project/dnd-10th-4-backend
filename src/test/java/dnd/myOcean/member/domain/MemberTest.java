package dnd.myOcean.member.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dnd.myOcean.member.domain.dto.request.InfoUpdateRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    private Member member;

    @BeforeEach
    void init() {
        member = Member.createFirstLoginMember("test@example.com");
    }

    @DisplayName("최초 로그인 회원의 경우 지정된 기본정보로 세팅된다.")
    @Test
    void testCreateFirstLoginMember() {
        // then
        assertNotNull(member);
        assertEquals("test@example.com", member.getEmail());
        assertEquals(Role.USER, member.getRole());
        assertEquals(Gender.NONE, member.getGender());
        assertEquals(0, member.getUpdateAgeCount());
        assertEquals(0, member.getUpdateGenderCount());
        assertEquals(5, member.getLetterCount());
    }

    @DisplayName("생일을 업데이트한다.")
    @Test
    void testUpdateBirthday() {
        // given
        String birthday = "1990-01-01";

        // when
        member.updateBirthday(birthday);

        // then
        assertEquals(LocalDate.parse(birthday), member.getBirthDay());
    }

    @DisplayName("성별을 업데이트한다.")
    @Test
    void testUpdateGender() {
        // when
        member.updateGender(Gender.MALE);

        // then
        assertEquals(Gender.MALE, member.getGender());
    }

    @DisplayName("닉네임을 업데이트한다.")
    @Test
    void testUpdateNickname() {
        // given
        String nickname = "고래";

        // when
        member.updateNickname(nickname);

        // then
        assertEquals("낯선 고래 " + member.getId(), member.getNickName());
    }

    @DisplayName("생일 변경 횟수를 초과하는 경우 true를 반환한다.")
    @Test
    void testIsBirthDayChangeLimitExceeded() {
        // when
        assertFalse(member.isBirthDayChangeLimitExceeded());
        member.updateBirthday("1999-04-22");
        member.updateBirthday("1997-04-22");

        // then
        assertTrue(member.isBirthDayChangeLimitExceeded());
    }

    @DisplayName("성별 변경 횟수를 초과하는 경우 true를 반환한다.")
    @Test
    void testIsGenderChangeLimitExceeded() {
        // when
        assertFalse(member.isGenderChangeLimitExceeded());
        member.updateGender(Gender.FEMALE);
        member.updateGender(Gender.MALE);

        // then
        assertTrue(member.isGenderChangeLimitExceeded());
    }

    @DisplayName("온보딩을 완료하는 경우, 첫 로그인이 아닌 것으로 간주한다.")
    @Test
    void testIsFirstLogin() {
        // when
        assertTrue(member.isFirstLogin());
        member.updateGender(Gender.MALE);

        // then
        assertFalse(member.isFirstLogin());
    }

    @DisplayName("보낼 수 있는 편지 개수가 0인 경우, true를 반환한다.")
    @Test
    void testExceedLetterLimit() throws Exception {
        assertFalse(member.exceedLetterLimit());

        Field letterCountField = Member.class.getDeclaredField("letterCount");
        letterCountField.setAccessible(true);
        letterCountField.set(member, 0);

        assertTrue(member.exceedLetterLimit());
    }

    @DisplayName("보낼 수 있는 편지 개수를 n으로 초기화한다.")
    @Test
    void testResetLetterCount() {
        member.resetLetterCount(10);
        assertEquals(10, member.getLetterCount());
    }

    @DisplayName("보낼 수 있는 편지 개수를 하나 줄인다.")
    @Test
    void testReduceLetterCount() {
        member.resetLetterCount(10);
        member.reduceLetterCount();
        assertEquals(9, member.getLetterCount());
    }

    @DisplayName("회원 정보를 업데이트한다.")
    @Test
    void testUpdateInfo() throws Exception {
        // given
        Constructor<InfoUpdateRequest> constructor = InfoUpdateRequest.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InfoUpdateRequest request = constructor.newInstance();

        request.setNickname("고래");
        request.setBirthday("1990-01-01");
        request.setGender("MALE");

        List<Worry> worries = new ArrayList<>();
        worries.add(Worry.builder()
                .worryType(WorryType.from("ETC"))
                .build()
        );

        // when
        member.updateInfo(request, worries);

        // then
        assertEquals("낯선 고래 " + member.getId(), member.getNickName());
        assertEquals(LocalDate.parse("1990-01-01"), member.getBirthDay());
        assertEquals(Gender.MALE, member.getGender());
        assertEquals(1, member.getWorries().size());
    }
}
