package dnd.myOcean.service.letter;


import dnd.myOcean.domain.letter.Letter;
import dnd.myOcean.domain.member.Member;
import dnd.myOcean.domain.worry.WorryType;
import dnd.myOcean.dto.letter.request.LetterSendRequest;
import dnd.myOcean.exception.member.MemberNotFoundException;
import dnd.myOcean.repository.jpa.letter.LetterRepository;
import dnd.myOcean.repository.jpa.member.MemberRepository;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LetterService {

    private static final Integer MAX_LETTER = 5;

    private final MemberRepository memberRepository;
    private final LetterRepository letterRepository;

    @Transactional
    public void sendLetter(LetterSendRequest request) {
        Member sender = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        List<Member> receivers = filterReceiver(request, memberRepository, sender);

        if (receivers.isEmpty()) {
            sendLetterWithoutFilterUpToMaxLetter(request, sender);
            return;
        }

        if (!receivers.isEmpty() && receivers.size() < MAX_LETTER) {
            sendLetterByFilteredUpToReceiversCount(request, receivers, sender);
            return;
        }

        sendLetterUptoMaxCount(receivers, request, sender);
    }

    private void sendLetterUptoMaxCount(List<Member> receivers, LetterSendRequest request,
                                        Member sender) {
        Collections.shuffle(receivers);
        for (int i = 0; i < MAX_LETTER; i++) {
            Letter letter = Letter.builder()
                    .content(request.getContent())
                    .sender(sender)
                    .receiver(receivers.get(i))
                    .isRead(false)
                    .hasReplied(false)
                    .build();
            letterRepository.save(letter);
        }
    }

    private void sendLetterByFilteredUpToReceiversCount(LetterSendRequest request, List<Member> receivers,
                                                        Member sender) {
        int n = generateRandomReceiverCount(receivers.size());
        Collections.shuffle(receivers);
        for (int i = 0; i < n; i++) {
            Letter letter = Letter.builder()
                    .content(request.getContent())
                    .sender(sender)
                    .receiver(receivers.get(i))
                    .isRead(false)
                    .hasReplied(false)
                    .build();
            letterRepository.save(letter);
        }
    }

    private void sendLetterWithoutFilterUpToMaxLetter(LetterSendRequest request, Member sender) {
        List<Member> randomReceivers = memberRepository.findRandomMembers(sender.getEmail(), MAX_LETTER);
        for (int i = 0; i < randomReceivers.size(); i++) {
            Letter letter = Letter.builder()
                    .content(request.getContent())
                    .sender(sender)
                    .receiver(randomReceivers.get(i))
                    .isRead(false)
                    .hasReplied(false)
                    .build();
            letterRepository.save(letter);
        }
    }

    private List<Member> filterReceiver(LetterSendRequest request, MemberRepository memberRepository, Member sender) {
        if (request.isEqualGender()) {
            return memberRepository.findFilteredAndSameGenderMember(
                    request.getAgeRangeStart(),
                    request.getAgeRangeEnd(),
                    sender.getGender(),
                    sender.getEmail(),
                    WorryType.from(request.getWorryType())
            );
        }

        return memberRepository.findFilteredMember(request.getAgeRangeStart(), request.getAgeRangeEnd(),
                request.getEmail(), WorryType.from(request.getWorryType()));
    }

    private int generateRandomReceiverCount(Integer maxCount) {
        return new Random().nextInt(maxCount) + 1;
    }
}
