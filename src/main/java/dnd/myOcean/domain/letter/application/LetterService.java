package dnd.myOcean.domain.letter.application;


import dnd.myOcean.domain.letter.alarm.event.LetterSendEvent;
import dnd.myOcean.domain.letter.domain.Letter;
import dnd.myOcean.domain.letter.domain.dto.request.LetterReplyRequest;
import dnd.myOcean.domain.letter.domain.dto.request.LetterSendRequest;
import dnd.myOcean.domain.letter.domain.dto.response.ReceivedLetterResponse;
import dnd.myOcean.domain.letter.domain.dto.response.RepliedLetterResponse;
import dnd.myOcean.domain.letter.domain.dto.response.SendLetterResponse;
import dnd.myOcean.domain.letter.exception.AccessDeniedLetterException;
import dnd.myOcean.domain.letter.exception.AlreadyReplyExistException;
import dnd.myOcean.domain.letter.exception.RepliedLetterPassException;
import dnd.myOcean.domain.letter.exception.UnAnsweredLetterStoreException;
import dnd.myOcean.domain.letter.repository.infra.jpa.LetterRepository;
import dnd.myOcean.domain.letter.repository.infra.querydsl.dto.LetterReadCondition;
import dnd.myOcean.domain.letter.repository.infra.querydsl.dto.PagedReceivedLettersResponse;
import dnd.myOcean.domain.letter.repository.infra.querydsl.dto.PagedRepliedLettersResponse;
import dnd.myOcean.domain.letter.repository.infra.querydsl.dto.PagedSendLettersResponse;
import dnd.myOcean.domain.member.domain.Member;
import dnd.myOcean.domain.member.domain.WorryType;
import dnd.myOcean.domain.member.exception.MemberNotFoundException;
import dnd.myOcean.domain.member.repository.infra.jpa.MemberRepository;
import dnd.myOcean.domain.report.domain.Report;
import dnd.myOcean.domain.report.repository.ReportRepository;
import dnd.myOcean.global.auth.aop.dto.CurrentMemberIdRequest;
import dnd.myOcean.global.exception.UnknownException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LetterService {

    private static final Integer MAX_LETTER = 5;

    private final ApplicationEventPublisher eventPublisher;
    private final MemberRepository memberRepository;
    private final LetterRepository letterRepository;
    private final ReportRepository reportRepository;

    // 0. 편지 전송
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

        eventPublisher.publishEvent(new LetterSendEvent(this, letters));
    }

    private void sendLetterUpToReceiversCount(LetterSendRequest request, List<Member> receivers,
                                              Member sender) {
        int letterMaxCount = generateRandomReceiverCount(receivers.size());
        Collections.shuffle(receivers);

        List<Optional<Report>> reporteds = receivers.stream().map(randomReceiver ->
                reportRepository.findByReporterAndReported(sender, randomReceiver)).collect(Collectors.toList());

        if (!reporteds.isEmpty()) {
            List<Member> members = reporteds.stream().map(reported -> reported.get().getReported()).collect(Collectors.toList());

            receivers.removeAll(members);
        }

        List<Letter> letters = createLetters(request, receivers, sender, letterMaxCount);
        letterRepository.saveAll(letters);

        eventPublisher.publishEvent(new LetterSendEvent(this, letters));
    }

    private void sendLetterUptoMaxCount(List<Member> receivers, LetterSendRequest request,
                                        Member sender) {
        Collections.shuffle(receivers);
        List<Letter> letters = createLetters(request, receivers, sender, MAX_LETTER);
        letterRepository.saveAll(letters);

        eventPublisher.publishEvent(new LetterSendEvent(this, letters));
    }

    private List<Letter> createLetters(LetterSendRequest request, List<Member> receivers, Member sender,
                                       int letterMaxCount) {
        String letterUuid = String.valueOf(UUID.randomUUID());
        return IntStream.range(0, letterMaxCount)
                .mapToObj(i -> Letter.createLetter(
                        sender,
                        request.getContent(),
                        receivers.get(i),
                        WorryType.from(request.getWorryType()),
                        letterUuid))
                .collect(Collectors.toList());
    }

    private int generateRandomReceiverCount(Integer maxCount) {
        return new Random().nextInt(maxCount) + 1;
    }

    // 1-1. 보낸 편지 단건 조회
    public SendLetterResponse readSendLetter(CurrentMemberIdRequest request, Long letterId) {
        Letter letter = letterRepository.findByIdAndSenderIdAndIsDeleteBySenderFalse(letterId, request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);
        return SendLetterResponse.toDto(letter);
    }

    // 1-2. 보낸 편지 삭제 (실제 삭제 X, 프로퍼티 값 변경)
    @Transactional
    public void deleteSendLetter(CurrentMemberIdRequest request, Long letterId) {
        Letter letter = letterRepository.findByIdAndSenderId(letterId, request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);
        letter.deleteBySender();
    }

    // 1-3. 보낸 편지 페이징 조회 (삭제하지 않은 메시지만 페이징)
    public PagedSendLettersResponse readSendLetters(LetterReadCondition cond) {
        return PagedSendLettersResponse.of(letterRepository.findAllSendLetter(cond));
    }

    // 2-1. 받은 편지 단건 조회
    public ReceivedLetterResponse readReceivedLetter(CurrentMemberIdRequest request, Long letterId) {
        Letter letter = letterRepository.findByIdAndReceiverIdAndHasRepliedIsFalse(letterId, request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);
        return ReceivedLetterResponse.toDto(letter);
    }

    // 2-2. 받은 편지 전체 조회
    public List<ReceivedLetterResponse> readReceivedLetters(CurrentMemberIdRequest request) {
        List<Letter> letters = letterRepository.findAllByReceiverIdAndHasRepliedFalse(request.getMemberId());

        return letters.stream()
                .map(letter -> ReceivedLetterResponse.toDto(letter))
                .collect(Collectors.toList());
    }

    // 2-3. 받은 편지에 대한 답장 설정
    @Transactional
    public void replyReceivedLetter(LetterReplyRequest request, Long letterId) {
        Letter letter = letterRepository.findByIdAndReceiverId(letterId,
                        request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);

        if (!letter.getReplyContent().isEmpty()) {
            throw new AlreadyReplyExistException();
        }

        letter.reply(request.getReplyContent());
    }

    // 2-4. 받은 편지 보관 (프로퍼티 값 변경) - 답장을 하지 않은 경우 해당 편지는 보관할 수 없음
    @Transactional
    public void storeReceivedLetter(CurrentMemberIdRequest request, Long letterId) {
        Letter letter = letterRepository.findByIdAndReceiverId(letterId, request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);

        if (!letter.isHasReplied()) {
            throw new UnAnsweredLetterStoreException();
        }

        letter.store(true);
    }

    // 2-5. 받은 편지 답장하지 않고 다른 사람에게 전달
    @Transactional
    public void passReceivedLetter(CurrentMemberIdRequest request, Long letterId) {
        Letter letter = letterRepository.findByIdAndReceiverId(letterId, request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);

        if (letter.isHasReplied()) {
            throw new RepliedLetterPassException();
        }

        // 전체 회원 id를 가져옴
        List<Long> memberIds = getAllMemberIds();

        // 해당 편지를 받은 사람의 id를 가져온다.
        List<Long> receiverIds = letterRepository.findAllByUuid(letter.getUuid())
                .stream()
                .map(l -> l.getReceiver().getId())
                .collect(Collectors.toList());

        // 전체 회원 id에서 해당 편지를 받은 사람과 해당 편지를 보낸 사람 제거
        memberIds.removeAll(receiverIds);
        memberIds.remove(letter.getReceiver().getId());

        Collections.shuffle(memberIds);
        Member newReceiver = memberRepository.findById(memberIds.get(0))
                .orElseThrow(UnknownException::new);

        letter.updateReceiver(newReceiver);
    }

    private List<Long> getAllMemberIds() {
        List<Long> memberIds = memberRepository.findAll()
                .stream()
                .map(Member::getId)
                .collect(Collectors.toList());
        return memberIds;
    }

    // 3-1. 보관한 편지 전체 페이징 조회
    public PagedReceivedLettersResponse readStoredLetters(LetterReadCondition cond) {
        return PagedReceivedLettersResponse.of(letterRepository.findAllStoredLetter(cond));
    }

    // 3-2. 보관한 편지 보관 해제
    @Transactional
    public void deleteStoredLetter(CurrentMemberIdRequest request, Long letterId) {
        Letter letter = letterRepository.findByIdAndReceiverId(letterId, request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);

        letter.store(false);
    }

    // 4-1. 답장 받은 편지 전체 조회
    public PagedRepliedLettersResponse readRepliedLetters(LetterReadCondition cond) {
        return PagedRepliedLettersResponse.of(letterRepository.findAllReliedLetter(cond));
    }

    // 4-2. 단건 조회
    @Transactional
    public RepliedLetterResponse readRepliedLetter(CurrentMemberIdRequest request, Long letterId) {
        Letter letter = letterRepository.findByIdAndReceiverIdAndHasRepliedTrue(letterId, request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);
        return RepliedLetterResponse.toDto(letter);
    }
}
