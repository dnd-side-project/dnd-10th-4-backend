package dnd.myOcean.domain.letter.alarm.event;

import dnd.myOcean.domain.letter.alarm.AlarmService;
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
    public void listen(LetterSendEvent event) {
        alarmService.alarmLetterReceived(event.getReceiversEmail());
    }
}
