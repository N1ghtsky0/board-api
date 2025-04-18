package xyz.jiwook.board.dao;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import xyz.jiwook.board.entity.QMemberEntity;
import xyz.jiwook.board.vo.UserInfoVO;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
public class MemberCustomRepoImpl implements MemberCustomRepo {
    private final JPAQueryFactory jpaQueryFactory;
    private final QMemberEntity qMemberEntity = QMemberEntity.memberEntity;

    @Override
    public UserInfoVO selectUserInfoByUsername(String username) {
        return Optional.ofNullable(
                jpaQueryFactory.select(Projections.constructor(UserInfoVO.class,
                                qMemberEntity.nickname, qMemberEntity.profileImage, qMemberEntity.introduction, formattedDate(qMemberEntity.createdAt)))
                        .from(qMemberEntity)
                        .where(qMemberEntity.username.eq(username))
                        .fetchOne())
                .orElse(new UserInfoVO());
    }

    private StringTemplate formattedDate(DateTimePath<LocalDateTime> createdAt) {
        return Expressions.stringTemplate("DATE_FORMAT({0}, {1})",
                createdAt,
                ConstantImpl.create("%Y-%m-%d"));
    }
}
