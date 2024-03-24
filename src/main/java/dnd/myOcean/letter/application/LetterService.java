package dnd.myOcean.letter.application;


import dnd.myOcean.global.auth.aop.dto.CurrentMemberIdRequest;
import dnd.myOcean.letter.alarm.event.LetterSendEvent;
import dnd.myOcean.letter.domain.Letter;
import dnd.myOcean.letter.domain.LetterTag;
import dnd.myOcean.letter.domain.dto.request.LetterReplyRequest;
import dnd.myOcean.letter.domain.dto.request.LetterSendRequest;
import dnd.myOcean.letter.domain.dto.request.SpecialLetterSendRequest;
import dnd.myOcean.letter.domain.dto.response.ReceivedLetterResponse;
import dnd.myOcean.letter.domain.dto.response.RepliedLetterResponse;
import dnd.myOcean.letter.domain.dto.response.SendLetterResponse;
import dnd.myOcean.letter.exception.AccessDeniedLetterException;
import dnd.myOcean.letter.exception.AlreadyReplyExistException;
import dnd.myOcean.letter.exception.PassDeniedLetterException;
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

    @Transactional
    public void sendByEmail(final SpecialLetterSendRequest request) throws IOException {
        Member sender = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        Member receiver = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        LetterImage letterImage = getLetterImage(request.getImage());

        letterRepository.save(Letter.createSpecialLetter(sender, receiver, letterImage, request.getContent()));
    }


    @Transactional
    public void send(final LetterSendRequest request) throws IOException {
        Member sender = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        if (sender.exceedLetterLimit()) {
            throw new ExceedSendLimitException();
        }

        sender.reduceLetterCount();

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

    private List<Member> filterReceiver(final LetterSendRequest request,
                                        final MemberRepository memberRepository,
                                        final Member sender) {
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

    private void sendLetterWithoutFilterUpToMaxLetter(final LetterSendRequest request, final Member sender)
            throws IOException {
        List<Member> randomReceivers = memberRepository.findRandomMembers(request.getMemberId(), MAX_LETTER);
        List<Letter> letters = createLetters(request, randomReceivers, sender, randomReceivers.size());
        letterRepository.saveAll(letters);

        eventPublisher.publishEvent(new LetterSendEvent(this, letters));
    }

    private void sendLetterUpToReceiversCount(final LetterSendRequest request,
                                              final List<Member> receivers,
                                              final Member sender) throws IOException {
        Collections.shuffle(receivers);
        List<Letter> letters = createLetters(request, receivers, sender, generateRandomReceiverCount(receivers.size()));
        letterRepository.saveAll(letters);

        eventPublisher.publishEvent(new LetterSendEvent(this, letters));
    }

    private void sendLetterUptoMaxCount(final List<Member> receivers,
                                        final LetterSendRequest request,
                                        final Member sender) throws IOException {
        Collections.shuffle(receivers);
        List<Letter> letters = createLetters(request, receivers, sender, MAX_LETTER);
        letterRepository.saveAll(letters);

        eventPublisher.publishEvent(new LetterSendEvent(this, letters));
    }

    private List<Letter> createLetters(final LetterSendRequest request, final List<Member> receivers,
                                       final Member sender, int letterCount) throws IOException {
        MultipartFile image = request.getImage();
        LetterImage letterImage = getLetterImage(image);

        LetterTag letterTag = LetterTag.of(
                request.getAgeRangeStart(),
                request.getAgeRangeEnd(),
                request.isEqualGender()
        );

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

    private LetterImage getLetterImage(final MultipartFile image) throws IOException {
        LetterImage letterImage;

        if (image != null) {
            letterImage = new LetterImage(image.getOriginalFilename());
            uploadImageToBucket(image, letterImage);
            return letterImage;
        }
        return null;
    }

    private void uploadImageToBucket(MultipartFile image, LetterImage letterImage) throws IOException {
        String imagePath = fileService.uploadImage(image, letterImage.getUniqueName());
        letterImage.updateImagePath(imagePath);
    }

    private int generateRandomReceiverCount(final Integer maxCount) {
        return new Random().nextInt(maxCount) + 1;
    }

    public SendLetterResponse readSendLetter(final CurrentMemberIdRequest request, final Long letterId) {
        Letter letter = letterRepository.findByIdAndSenderIdAndIsDeleteBySenderFalse(letterId, request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);
        return SendLetterResponse.toDto(letter);
    }

    @Transactional
    public void deleteSendLetter(final CurrentMemberIdRequest request, final Long letterId) {
        Letter letter = letterRepository.findByIdAndSenderId(letterId, request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);

        List<Letter> sendLetters = letterRepository.findAllByUuid(letter.getUuid());
        sendLetters.forEach(l -> l.deleteBySender());
    }

    public PagedSendLettersResponse readSendLetters(final LetterReadCondition cond) {
        return PagedSendLettersResponse.of(letterRepository.findAllSendLetter(cond));
    }

    public ReceivedLetterResponse readReceivedLetter(final CurrentMemberIdRequest request, final Long letterId) {
        Letter letter = letterRepository.findByIdAndReceiverIdAndHasRepliedIsFalse(letterId, request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);
        return ReceivedLetterResponse.toDto(letter);
    }

    public List<ReceivedLetterResponse> readReceivedLetters(final CurrentMemberIdRequest request) {
        List<Letter> letters = letterRepository.findAllByReceiverIdAndHasRepliedFalseAndStoredFalse(
                request.getMemberId());

        return letters.stream()
                .map(letter -> ReceivedLetterResponse.toDto(letter))
                .collect(Collectors.toList());
    }

    @Transactional
    public void replyReceivedLetter(final LetterReplyRequest request, final Long letterId) throws IOException {
        Letter letter = letterRepository.findByIdAndReceiverId(letterId,
                        request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);

        if (letter.isHasReplied()) {
            throw new AlreadyReplyExistException();
        }

        MultipartFile image = request.getImage();
        LetterImage letterImage = getLetterImage(image);
        letter.reply(request.getReplyContent(), letterImage);
    }

    @Transactional
    public void passReceivedLetter(final CurrentMemberIdRequest request, final Long letterId) {
        Letter letter = letterRepository.findByIdAndReceiverId(letterId, request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);

        if (letter.isHasReplied()) {
            throw new RepliedLetterPassException();
        }

        if (!letter.isNormalLetter()) {
            throw new PassDeniedLetterException();
        }

        List<Long> memberIds = getAllMemberIds();

        List<Long> receiverIds = letterRepository.findAllByUuid(letter.getUuid())
                .stream()
                .map(l -> l.getReceiver().getId())
                .collect(Collectors.toList());

        memberIds.removeAll(receiverIds);
        memberIds.remove(letter.getReceiver().getId());

        Collections.shuffle(memberIds);
        Member newReceiver = memberRepository.findById(memberIds.get(0))
                .orElseThrow(MemberNotFoundException::new);

        letter.updateReceiver(newReceiver);
    }

    private List<Long> getAllMemberIds() {
        List<Long> memberIds = memberRepository.findAll()
                .stream()
                .map(Member::getId)
                .collect(Collectors.toList());
        return memberIds;
    }


    public List<RepliedLetterResponse> readRepliedLetters(final CurrentMemberIdRequest request) {
        return RepliedLetterResponse.toDtoList(
                letterRepository.findAllBySenderIdAndHasRepliedTrueAndStoredFalse(request.getMemberId())
        );
    }

    @Transactional
    public RepliedLetterResponse readRepliedLetter(final CurrentMemberIdRequest request, final Long letterId) {
        Letter letter = letterRepository.findByIdAndSenderIdAndHasRepliedTrueAndStoredFalse(
                        letterId,
                        request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);
        return RepliedLetterResponse.toDto(letter);
    }

    @Transactional
    public void storeRepliedLetter(final CurrentMemberIdRequest request, final Long letterId) {
        Letter letter = letterRepository.findByIdAndSenderIdAndHasRepliedTrueAndStoredFalse(
                        letterId,
                        request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);

        letter.store(true);
    }

    @Transactional
    public void deleteStoredLetter(final CurrentMemberIdRequest request, final Long letterId) {
        Letter letter = letterRepository.findByIdAndSenderIdAndHasRepliedTrueAndStoredTrue(letterId,
                        request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);

        letter.store(false);
    }

    public PagedStoredLetterResponse readStoredLetters(final LetterReadCondition cond) {
        return PagedStoredLetterResponse.of(letterRepository.findAllStoredLetter(cond));
    }

    @Transactional
    public void storeOnboardingLetter(final CurrentMemberIdRequest request, final Long letterId) {
        Letter letter = letterRepository.findByIdAndReceiverIdAndOnboardingLetter(
                        letterId,
                        request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);

        if (letter.isStored()) {
            letter.store(false);
            return;
        }

        letter.store(true);
    }
}
