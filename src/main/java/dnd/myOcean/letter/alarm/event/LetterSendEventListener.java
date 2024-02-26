package dnd.myOcean.letter.alarm.event;

import dnd.myOcean.letter.alarm.AlarmService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class LetterSendEventListener {

    private final AlarmService alarmService;

    @Async
    @TransactionalEventListener(classes = LetterSendEvent.class,
            phase = TransactionPhase.AFTER_COMMIT)
    public void listen(LetterSendEvent event) throws MessagingException {
        alarmService.alarmLetterReceived(event.getReceiversEmail());
    }
}
