package dnd.myOcean.domain.member.repository.infra.jpa;

import dnd.myOcean.domain.member.domain.Gender;
import dnd.myOcean.domain.member.domain.Member;
import dnd.myOcean.domain.member.domain.WorryType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickName(String nickName);

    @Query("SELECT DISTINCT m FROM Member m "
            + "JOIN FETCH m.worries memberworry "
            + "JOIN FETCH memberworry.worry worry "
            + "WHERE worry.worryType = :worryType "
            + "AND m.age BETWEEN :minAge AND :maxAge "
            + "AND m.gender = :gender "
            + "AND m.email != :email")
    List<Member> findFilteredAndSameGenderMember(@Param("minAge") int minAge, @Param("maxAge") int maxAge,
                                                 @Param("gender") Gender gender, @Param("email") String email,
                                                 @Param("worryType") WorryType worryType);

    @Query("SELECT DISTINCT m FROM Member m "
            + "JOIN FETCH m.worries memberworry "
            + "JOIN FETCH memberworry.worry worry "
            + "WHERE worry.worryType = :worryType "
            + "AND m.age BETWEEN :minAge AND :maxAge "
            + "AND m.email != :email")
    List<Member> findFilteredMember(@Param("minAge") int minAge,
                                    @Param("maxAge") int maxAge,
                                    @Param("email") String email,
                                    @Param("worryType") WorryType worryType);

    @Query(value = "SELECT m FROM Member m WHERE m.email != :email ORDER BY RAND() LIMIT :n", nativeQuery = false)
    List<Member> findRandomMembers(@Param("email") String email, @Param("n") int n);
}
