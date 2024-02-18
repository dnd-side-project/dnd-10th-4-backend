package dnd.myOcean.domain.letter.repository.infra.jpa;

import dnd.myOcean.domain.letter.domain.Letter;
import dnd.myOcean.domain.letter.repository.infra.querydsl.LetterQuerydslRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LetterRepository extends JpaRepository<Letter, Long>, LetterQuerydslRepository {

    @Query("SELECT l FROM Letter l JOIN FETCH l.receiver lr WHERE lr.id = :receiverId AND l.hasReplied = false")
    List<Letter> findAllByReceiverIdAndHasRepliedFalse(@Param("receiverId") Long receiverId);

    @Query("SELECT l FROM Letter l JOIN FETCH l.sender sr WHERE sr.id = :senderId AND l.hasReplied = true")
    List<Letter> findAllBySenderIdAndHasRepliedTrue(@Param("senderId") Long senderId);

    @Query("SELECT l FROM Letter l JOIN FETCH l.sender ls WHERE l.id = :id AND ls.id = :senderId")
    Optional<Letter> findByIdAndSenderId(@Param("id") Long id,
                                         @Param("senderId") Long senderId);

    @Query("SELECT l FROM Letter l JOIN FETCH l.sender ls WHERE l.id = :id AND ls.id = :senderId AND l.isDeleteBySender = false")
    Optional<Letter> findByIdAndSenderIdAndIsDeleteBySenderFalse(@Param("id") Long id,
                                                                 @Param("senderId") Long senderId);

    @Query("SELECT l FROM Letter l JOIN FETCH l.receiver lr WHERE l.id = :id AND lr.id = :receiverId")
    Optional<Letter> findByIdAndReceiverId(@Param("id") Long id,
                                           @Param("receiverId") Long receiverId);

    @Query("SELECT l FROM Letter l JOIN FETCH l.sender ls WHERE l.id = :id AND ls.id = :senderId AND l.hasReplied = true")
    Optional<Letter> findByIdAndSenderIdAndHasRepliedTrue(@Param("id") Long id,
                                                          @Param("senderId") Long senderId);

    @Query("SELECT l FROM Letter l JOIN FETCH l.receiver lr WHERE l.id = :id AND lr.id = :receiverId AND l.hasReplied = false")
    Optional<Letter> findByIdAndReceiverIdAndHasRepliedIsFalse(@Param("id") Long id,
                                                               @Param("receiverId") Long receiverId);

    @Query("SELECT l FROM Letter l WHERE l.uuid = :uuid")
    List<Letter> findAllByUuid(@Param("uuid") String uuid);

    @Modifying
    @Query("DELETE FROM Letter l WHERE l.createDate <= :expirationDate and l.hasReplied = false")
    void deleteDiscardedLetters(@Param("expirationDate") LocalDateTime expirationDate);
}
