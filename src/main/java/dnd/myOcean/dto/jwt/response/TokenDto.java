package dnd.myOcean.dto.jwt.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenDto {

    private final String accessToken;
    private final String refreshToken;
}
