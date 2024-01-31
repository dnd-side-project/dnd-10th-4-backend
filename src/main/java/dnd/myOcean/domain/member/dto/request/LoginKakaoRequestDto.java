package dnd.myOcean.domain.member.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginKakaoRequestDto {

    private String email;
}
