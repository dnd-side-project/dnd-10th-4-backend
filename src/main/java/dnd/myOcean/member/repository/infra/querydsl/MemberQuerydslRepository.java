package dnd.myOcean.member.repository.infra.querydsl;

import dnd.myOcean.member.domain.dto.response.MemberInfoResponse;
import dnd.myOcean.member.repository.infra.querydsl.dto.MemberReadCondition;
import org.springframework.data.domain.Page;

public interface MemberQuerydslRepository {

    Page<MemberInfoResponse> findAllMember(MemberReadCondition cond);
}
