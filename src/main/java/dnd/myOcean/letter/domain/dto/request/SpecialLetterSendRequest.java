package dnd.myOcean.letter.domain.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpecialLetterSendRequest {

    @Null
    private Long memberId;

    @NotEmpty(message = "받을 사람의 이메일을 입력해주세요.")
    private String email;

    @NotEmpty(message = "편지 내용을 입력해주세요.")
    private String content;

    @Nullable
    private MultipartFile image;
}
