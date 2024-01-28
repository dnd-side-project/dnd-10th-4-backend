package dnd.myOcean.repository.redis;

import dnd.myOcean.domain.refreshtoken.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, Long> {

    RefreshToken findByRefreshToken(String refreshToken);
}
