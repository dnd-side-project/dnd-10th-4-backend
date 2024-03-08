package dnd.myOcean.global.auth.jwt.token;

import lombok.Getter;

@Getter
public class LoginResponse extends TokenResponse {

    private final boolean isFirstLogin;

    private LoginResponse(final String accessToken, final String refreshToken, boolean isFirstLogin) {
        super(accessToken, refreshToken);
        this.isFirstLogin = isFirstLogin;
    }

    public static LoginResponse of(final String accessToken, final String refreshToken, final boolean isFirstLogin) {
        return new LoginResponse(accessToken, refreshToken, isFirstLogin);
    }
}
