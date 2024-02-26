package dnd.myOcean.letter.alarm;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlarmService {

    @Autowired
    private final JavaMailSender javaMailSender;

    public void alarmLetterReceived(List<String> receiversEmail) {
        String msg = "";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("\uD83C\uDF0A새로운 편지가 흘러왔어요 \uD83D\uDC8C");
        msg += "\uD83C\uDF0A새로운 편지가 흘러왔어요 \uD83D\uDC8C \n" +
                "\n" +
                "자세한 내용은 내 마음속 바다에서 확인해주세요\n" +
                "\n" +
                "\uD83D\uDC49 <a href=\"https://sea-of-my-heart.vercel.app/\"> 내 마음 속 바다 바로가기 </a>";
        message.setText(msg);

        receiversEmail.forEach(receiverEmail -> {
            message.setTo(receiverEmail);
            javaMailSender.send(message);
        });
    }
}
