package dnd.myOcean.global.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlarmService {

    private final JavaMailSender javaMailSender;

    public void alarmLetterReceived(String receiverEmail) {
        String msg = "";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("[My Ocean] 새로운 편지가 도착했습니다.");
        message.setTo(receiverEmail);
        msg += "[My Ocean] 새로운 편지가 도착했습니다. 자세한 내용은 앱에서 확인해주세요.";
        message.setText(msg);
        javaMailSender.send(message);
    }
}
