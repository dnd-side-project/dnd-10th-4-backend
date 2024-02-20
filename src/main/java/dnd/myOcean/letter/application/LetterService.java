package dnd.myOcean.letter.application;


import dnd.myOcean.global.auth.aop.dto.CurrentMemberIdRequest;
import dnd.myOcean.global.exception.UnknownException;
import dnd.myOcean.letter.alarm.event.LetterSendEvent;
import dnd.myOcean.letter.domain.Letter;
import dnd.myOcean.letter.domain.LetterTag;
import dnd.myOcean.letter.domain.dto.request.LetterReplyRequest;
import dnd.myOcean.letter.domain.dto.request.LetterSendRequest;
import dnd.myOcean.letter.domain.dto.response.ReceivedLetterResponse;
import dnd.myOcean.letter.domain.dto.response.RepliedLetterResponse;
import dnd.myOcean.letter.domain.dto.response.SendLetterResponse;
import dnd.myOcean.letter.exception.AccessDeniedLetterException;
import dnd.myOcean.letter.exception.AlreadyReplyExistException;
import dnd.myOcean.letter.exception.RepliedLetterPassException;
import dnd.myOcean.letter.repository.infra.jpa.LetterRepository;
import dnd.myOcean.letter.repository.infra.querydsl.dto.LetterReadCondition;
import dnd.myOcean.letter.repository.infra.querydsl.dto.PagedSendLettersResponse;
import dnd.myOcean.letter.repository.infra.querydsl.dto.PagedStoredLetterResponse;
import dnd.myOcean.letterimage.application.FileService;
import dnd.myOcean.letterimage.domain.LetterImage;
import dnd.myOcean.member.domain.Member;
import dnd.myOcean.member.domain.WorryType;
import dnd.myOcean.member.exception.ExceedSendLimitException;
import dnd.myOcean.member.exception.MemberNotFoundException;
import dnd.myOcean.member.repository.infra.jpa.MemberRepository;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LetterService {

    private static final Integer MAX_LETTER = 5;

    private final FileService fileService;
    private final ApplicationEventPublisher eventPublisher;
    private final MemberRepository memberRepository;
    private final LetterRepository letterRepository;

    // 편지 전송
    @Transactional
    public void send(LetterSendRequest request) throws IOException {
        Member sender = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        if (sender.exceedLetterLimit()) {
            throw new ExceedSendLimitException();
        }

        sender.updateLetterCount();

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
            return memberRepository.findFilteredAndSameGenderMember(
                    request.getAgeRangeStart(),
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

    private void sendLetterWithoutFilterUpToMaxLetter(LetterSendRequest request, Member sender) throws IOException {
        List<Member> randomReceivers = memberRepository.findRandomMembers(request.getMemberId(), MAX_LETTER);
        List<Letter> letters = createLetters(request, randomReceivers, sender, randomReceivers.size());
        letterRepository.saveAll(letters);

        eventPublisher.publishEvent(new LetterSendEvent(this, letters));
    }

    private void sendLetterUpToReceiversCount(LetterSendRequest request, List<Member> receivers,
                                              Member sender) throws IOException {
        int letterCount = generateRandomReceiverCount(receivers.size());
        Collections.shuffle(receivers);
        List<Letter> letters = createLetters(request, receivers, sender, letterCount);
        letterRepository.saveAll(letters);

        eventPublisher.publishEvent(new LetterSendEvent(this, letters));
    }

    private void sendLetterUptoMaxCount(List<Member> receivers, LetterSendRequest request,
                                        Member sender) throws IOException {
        Collections.shuffle(receivers);
        List<Letter> letters = createLetters(request, receivers, sender, MAX_LETTER);
        letterRepository.saveAll(letters);

        eventPublisher.publishEvent(new LetterSendEvent(this, letters));
    }

    private List<Letter> createLetters(LetterSendRequest request, List<Member> receivers, Member sender,
                                       int letterCount) throws IOException {
        MultipartFile image = request.getImage();
        LetterImage letterImage = getLetterImage(image);

        LetterTag letterTag = LetterTag.of(request.getAgeRangeStart(),
                request.getAgeRangeEnd(),
                request.isEqualGender());

        String letterUuid = String.valueOf(UUID.randomUUID());
        return IntStream.range(0, letterCount)
                .mapToObj(i -> Letter.createLetter(
                        sender,
                        request.getContent(),
                        receivers.get(i),
                        WorryType.from(request.getWorryType()),
                        letterTag,
                        letterImage,
                        letterUuid))
                .collect(Collectors.toList());
    }

    private LetterImage getLetterImage(MultipartFile image) throws IOException {
        LetterImage letterImage;

        if (image != null) {
            letterImage = new LetterImage(image.getOriginalFilename());
            String imagePath = fileService.uploadImage(image, letterImage.getUniqueName());
            letterImage.updateImagePath(imagePath);
            return letterImage;
        }
        return null;
    }

    private int generateRandomReceiverCount(Integer maxCount) {
        return new Random().nextInt(maxCount) + 1;
    }

    // 보낸 편지 단건 조회
    public SendLetterResponse readSendLetter(CurrentMemberIdRequest request, Long letterId) {
        Letter letter = letterRepository.findByIdAndSenderIdAndIsDeleteBySenderFalse(letterId, request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);
        return SendLetterResponse.toDto(letter);
    }

    // 보낸 편지 삭제 (실제 삭제 X, 프로퍼티 값 변경)
    @Transactional
    public void deleteSendLetter(CurrentMemberIdRequest request, Long letterId) {
        Letter letter = letterRepository.findByIdAndSenderId(letterId, request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);
        letter.deleteBySender();
    }

    // 보낸 편지 페이징 조회 (삭제하지 않은 메시지만 페이징)
    public PagedSendLettersResponse readSendLetters(LetterReadCondition cond) {
        return PagedSendLettersResponse.of(letterRepository.findAllSendLetter(cond));
    }

    // 받은 편지 단건 조회
    public ReceivedLetterResponse readReceivedLetter(CurrentMemberIdRequest request, Long letterId) {
        Letter letter = letterRepository.findByIdAndReceiverIdAndHasRepliedIsFalse(letterId, request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);
        return ReceivedLetterResponse.toDto(letter);
    }

    // 받은 편지 전체 조회
    public List<ReceivedLetterResponse> readReceivedLetters(CurrentMemberIdRequest request) {
        List<Letter> letters = letterRepository.findAllByReceiverIdAndHasRepliedFalse(request.getMemberId());

        return letters.stream()
                .map(letter -> ReceivedLetterResponse.toDto(letter))
                .collect(Collectors.toList());
    }

    // 받은 편지에 대한 답장 설정
    @Transactional
    public void replyReceivedLetter(LetterReplyRequest request, Long letterId) throws IOException {
        Letter letter = letterRepository.findByIdAndReceiverId(letterId,
                        request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);

        if (letter.getReplyContent() != null) {
            throw new AlreadyReplyExistException();
        }

        MultipartFile image = request.getImage();
        LetterImage letterImage = getLetterImage(image);
        letter.reply(request.getReplyContent(), letterImage);
    }

    // 받은 편지 답장하지 않고 다른 사람에게 전달
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


    // 답장 받은 편지 전체 조회
    public List<RepliedLetterResponse> readRepliedLetters(CurrentMemberIdRequest request) {
        return RepliedLetterResponse.toDtoList(
                letterRepository.findAllBySenderIdAndHasRepliedTrueAndStoredFalse(request.getMemberId())
        );
    }

    // 답장 받은 단건 조회
    @Transactional
    public RepliedLetterResponse readRepliedLetter(CurrentMemberIdRequest request, Long letterId) {
        Letter letter = letterRepository.findByIdAndSenderIdAndHasRepliedTrue(letterId, request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);
        return RepliedLetterResponse.toDto(letter);
    }

    // 답장 받은 편지 보관
    @Transactional
    public void storeRepliedLetter(CurrentMemberIdRequest request, Long letterId) {
        Letter letter = letterRepository.findByIdAndSenderIdAndHasRepliedTrue(letterId, request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);

        letter.store(true);
    }

    // 보관한 편지 보관 해제
    @Transactional
    public void deleteStoredLetter(CurrentMemberIdRequest request, Long letterId) {
        Letter letter = letterRepository.findByIdAndSenderIdAndHasRepliedTrue(letterId, request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);

        letter.store(false);
    }

    // 보관한 편지 전체 페이징 조회
    public PagedStoredLetterResponse readStoredLetters(LetterReadCondition cond) {
        return PagedStoredLetterResponse.of(letterRepository.findAllStoredLetter(cond));
    }
}
