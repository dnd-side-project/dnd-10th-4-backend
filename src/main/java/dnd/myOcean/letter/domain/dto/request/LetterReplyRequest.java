package dnd.myOcean.letter.domain.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LetterReplyRequest {

    @Null
    private Long memberId;

    @NotBlank(message = "답장 내용을 입력해주세요.")
    private String replyContent;

    @Nullable
    private MultipartFile image;
}
