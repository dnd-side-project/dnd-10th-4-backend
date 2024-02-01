package dnd.myOcean.domain.letter.application;


import dnd.myOcean.domain.letter.domain.Letter;
import dnd.myOcean.domain.letter.dto.request.LetterDeleteRequest;
import dnd.myOcean.domain.letter.dto.request.LetterReadRequest;
import dnd.myOcean.domain.letter.dto.request.LetterSendRequest;
import dnd.myOcean.domain.letter.dto.response.LetterResponse;
import dnd.myOcean.domain.letter.exception.AccessDeniedLetterException;
import dnd.myOcean.domain.letter.repository.infra.jpa.LetterRepository;
import dnd.myOcean.domain.member.domain.Member;
import dnd.myOcean.domain.member.domain.WorryType;
import dnd.myOcean.domain.member.exception.MemberNotFoundException;
import dnd.myOcean.domain.member.repository.infra.jpa.MemberRepository;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LetterService {

    private static final Integer MAX_LETTER = 5;

    private final MemberRepository memberRepository;
    private final LetterRepository letterRepository;

    // 편지 전송 -> 받은 사람들에게 이메일 알림
    @Transactional
    public void send(LetterSendRequest request) {
        Member sender = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        List<Member> receivers = filterReceiver(request, memberRepository, sender);

        if (receivers.isEmpty()) {
            sendLetterWithoutFilterUpToMaxLetter(request, sender);
            return;
        }

        if (!receivers.isEmpty() && receivers.size() < MAX_LETTER) {
            sendLetterUpToReceiversCount(request, receivers, sender);
            return;
        }

        sendLetterUptoMaxCount(receivers, request, sender);
    }

    private List<Member> filterReceiver(LetterSendRequest request, MemberRepository memberRepository, Member sender) {
        if (request.isEqualGender()) {
            return memberRepository.findFilteredAndSameGenderMember(request.getAgeRangeStart(),
                    request.getAgeRangeEnd(),
                    sender.getGender(),
                    sender.getId(),
                    WorryType.from(request.getWorryType())
            );
        }

        return memberRepository.findFilteredMember(
                request.getAgeRangeStart(),
                request.getAgeRangeEnd(),
                request.getMemberId(),
                WorryType.from(request.getWorryType()));
    }

    private void sendLetterWithoutFilterUpToMaxLetter(LetterSendRequest request, Member sender) {
        List<Member> randomReceivers = memberRepository.findRandomMembers(sender.getEmail(), MAX_LETTER);
        List<Letter> letters = createLetters(request, randomReceivers, sender, randomReceivers.size());
        letterRepository.saveAll(letters);
    }

    private void sendLetterUpToReceiversCount(LetterSendRequest request, List<Member> receivers,
                                              Member sender) {
        int letterMaxCount = generateRandomReceiverCount(receivers.size());
        Collections.shuffle(receivers);
        List<Letter> letters = createLetters(request, receivers, sender, letterMaxCount);

        letterRepository.saveAll(letters);
    }

    private void sendLetterUptoMaxCount(List<Member> receivers, LetterSendRequest request,
                                        Member sender) {
        Collections.shuffle(receivers);
        List<Letter> letters = createLetters(request, receivers, sender, MAX_LETTER);

        letterRepository.saveAll(letters);
    }

    private static List<Letter> createLetters(LetterSendRequest request, List<Member> receivers, Member sender,
                                              int letterMaxCount) {
        return IntStream.range(0, letterMaxCount)
                .mapToObj(i -> Letter.createLetter(
                        sender,
                        request.getContent(),
                        receivers.get(i),
                        WorryType.from(request.getWorryType())
                ))
                .collect(Collectors.toList());
    }

    private int generateRandomReceiverCount(Integer maxCount) {
        return new Random().nextInt(maxCount) + 1;
    }

    // 보낸 편지
    // 1. 단건 조회
    // 2. 삭제 (실제 삭제 X, 프로퍼티 값 변경)
    // 3. 전체 페이징 조회(삭제하지 않은 메시지만 페이징)
    @Transactional
    public LetterResponse readSendLetter(LetterReadRequest request, Long letterId) {
        Letter letter = letterRepository.findByIdAndSenderId(letterId, request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);
        return LetterResponse.toDto(letter);
    }

    @Transactional
    public void deleteSendLetter(LetterDeleteRequest request, Long letterId) {
        Letter letter = letterRepository.findByIdAndSenderId(letterId, request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);
        letter.deleteBySender();
    }

    // 받은 편지
    // 1. 단건 조회(프로퍼티 값 변경)
    // 2. 전체 조회
    // 3. 받은 편지 보관 (프로퍼티 값 변경)
    @Transactional
    public LetterResponse readReceivedLetter(LetterReadRequest request, Long letterId) {
        Letter letter = letterRepository.findByIdAndReceiverIdAndDeleteByReceiverIsFalse(letterId,
                        request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);
        letter.read();

        return LetterResponse.toDto(letter);
    }

    // 받은 편지에 대한 답장 설정 -> 보낸 사람에게 이메일 알림

    // 받은 편지 다른 사람에게 토스 -> 받은 사람들에게 이메일 알림

    // 보관한 편지
    // 1. 단건 조회
    // 2. 전체 페이징 조회
    // 3. 보관한 편지 삭제

    // 답장 받은 편지
    // 1. 전체 조회
    // 2. 단건 조회
}
