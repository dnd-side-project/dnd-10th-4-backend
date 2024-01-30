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

    private static final Integer MAX_COUNT = 5;

    private final MemberRepository memberRepository;
    private final LetterRepository letterRepository;

    @Transactional
    public void sendLetter(LetterSendRequest request) {
        Member sender = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        // 필터링한 결과
        List<Member> receivers = filterReceiver(request, memberRepository, sender);

        for (Member receiver : receivers) {
            System.out.println(receiver.getEmail());
        }

        // 편지를 받을 사람이 존재하지 않는 경우
        if (receivers.isEmpty()) {
            // 자신을 제외한 5명에게 무작위로 보낸다.
            List<Member> randomReceivers = memberRepository.findRandomMembers(sender.getEmail(), MAX_COUNT);

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
            return;
        }

        // 필터링한 수신자가 존재하고, 수신자 수가 지정한 최대 수신자 사이즈보다 작은 경우
        if (!receivers.isEmpty() && receivers.size() < MAX_COUNT) {
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
            return;
        }

        // 필터링한 수신자가 존재하고, 수신자 수가 지정한 최대 수신자 사이즈보다 크거나 같은 경우
        if (!receivers.isEmpty() && receivers.size() >= MAX_COUNT) {
            Collections.shuffle(receivers);
            for (int i = 0; i < MAX_COUNT; i++) {
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
