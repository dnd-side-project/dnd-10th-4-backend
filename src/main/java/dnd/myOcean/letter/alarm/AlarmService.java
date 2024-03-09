package dnd.myOcean.letter.alarm;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlarmService {

    private final JavaMailSender javaMailSender;

    public void alarmLetterReceived(final List<String> receiversEmail) throws MessagingException {
        for (String receiverEmail : receiversEmail) {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            helper.setTo(receiverEmail);
            helper.setSubject("\uD83C\uDF0A새로운 편지가 흘러왔어요. \uD83D\uDC8C");

            String msg = "<h1> \uD83C\uDF0A 새로운 편지가 흘러왔어요. \uD83D\uDC8C </h1>"
                    + "<br> 자세한 내용은 내 마음속 바다에서 확인해주세요. <br/>"
                    + "\uD83D\uDC49 내 마음 속 바다 <a href=\"https://sea-of-my-heart.vercel.app/\"> 바로가기 </a>";

            helper.setText(msg, true);
            javaMailSender.send(message);
        }
    }
}
