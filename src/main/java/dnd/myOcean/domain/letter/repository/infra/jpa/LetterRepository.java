package dnd.myOcean.domain.letter.repository.infra.jpa;

import dnd.myOcean.domain.letter.domain.Letter;
import dnd.myOcean.domain.letter.repository.infra.querydsl.LetterQuerydslRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface LetterRepository extends JpaRepository<Letter, Long>, LetterQuerydslRepository {

    List<Letter> findAllByReceiverIdAndHasRepliedFalse(@Param("receiverId") Long receiverId);

    Optional<Letter> findByIdAndSenderId(@Param("id") Long id,
                                         @Param("senderId") Long senderId);

    Optional<Letter> findByIdAndSenderIdAndIsDeleteBySenderFalse(@Param("id") Long id,
                                                                 @Param("senderId") Long senderId);

    Optional<Letter> findByIdAndReceiverId(@Param("id") Long id,
                                           @Param("receiverId") Long receiverId);

    Optional<Letter> findByIdAndReceiverIdAndHasRepliedTrue(@Param("id") Long id,
                                                            @Param("receiverId") Long receiverId);

    List<Letter> findAllByUuid(@Param("uuid") String uuid);
}
