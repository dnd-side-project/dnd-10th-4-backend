package dnd.myOcean.global.alarm.event;

import dnd.myOcean.domain.letter.domain.Letter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class LetterSendEvent extends ApplicationEvent {

    private final List<String> receiversEmail;

    public LetterSendEvent(Object source, List<Letter> letters) {
        super(source);
        this.receiversEmail = letters.stream().map(letter -> letter.getReceiver().getEmail())
                .collect(Collectors.toList());
    }
}
