package dnd.myOcean.dto.sign;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@Getter
public class MemberCreateRequest {

    @NotBlank(message = "아이디를 입력하세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;

    @NotBlank(message = "닉네임을 입력하세요.")
    private String nickname;
}
