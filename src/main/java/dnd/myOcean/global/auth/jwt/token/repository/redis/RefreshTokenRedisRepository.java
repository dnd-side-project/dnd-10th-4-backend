package dnd.myOcean.global.auth.jwt.token.repository.redis;

import dnd.myOcean.global.common.auth.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, Long> {

    RefreshToken findByRefreshToken(String refreshToken);
}
