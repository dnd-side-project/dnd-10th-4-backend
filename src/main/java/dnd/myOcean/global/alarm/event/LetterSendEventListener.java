package dnd.myOcean.global.alarm.event;

import dnd.myOcean.global.alarm.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class LetterSendEventListener {

    private final AlarmService alarmService;

    @Async("alarmSender") // 비동기 처리로 속도 Up
    @TransactionalEventListener(classes = LetterSendEvent.class,
            phase = TransactionPhase.AFTER_COMMIT)
    public void listen(LetterSendEvent event) {
        alarmService.alarmLetterReceived(event.getReceiversEmail());
    }
}
