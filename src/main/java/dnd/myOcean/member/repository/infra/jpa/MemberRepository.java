package dnd.myOcean.member.repository.infra.jpa;

import dnd.myOcean.member.domain.Gender;
import dnd.myOcean.member.domain.Member;
import dnd.myOcean.member.domain.WorryType;
import dnd.myOcean.member.repository.infra.querydsl.MemberQuerydslRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQuerydslRepository {

    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m WHERE m.nickName = '낯선 바다'")
    Optional<Member> findRootUser();

    @Query("SELECT DISTINCT m FROM Member m "
            + "JOIN FETCH m.worries memberworry "
            + "JOIN FETCH memberworry.worry worry "
            + "WHERE worry.worryType = :worryType "
            + "AND m.age BETWEEN :minAge AND :maxAge "
            + "AND m.gender = :gender "
            + "AND m.id != :id")
    List<Member> findFilteredAndSameGenderMember(@Param("minAge") int minAge, @Param("maxAge") int maxAge,
                                                 @Param("gender") Gender gender, @Param("id") long id,
                                                 @Param("worryType") WorryType worryType);

    @Query("SELECT DISTINCT m FROM Member m "
            + "JOIN FETCH m.worries memberworry "
            + "JOIN FETCH memberworry.worry worry "
            + "WHERE worry.worryType = :worryType "
            + "AND m.age BETWEEN :minAge AND :maxAge "
            + "AND m.id != :id")
    List<Member> findFilteredMember(@Param("minAge") int minAge,
                                    @Param("maxAge") int maxAge,
                                    @Param("id") long id,
                                    @Param("worryType") WorryType worryType);

    @Query(value = "SELECT m FROM Member m WHERE m.id != :id ORDER BY RAND() LIMIT :n", nativeQuery = false)
    List<Member> findRandomMembers(@Param("id") Long id, @Param("n") int n);
}
