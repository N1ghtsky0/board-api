package xyz.jiwook.board.dao;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import xyz.jiwook.board.entity.QPostEntity;
import xyz.jiwook.board.vo.PostVO;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class PostCustomRepoImpl implements PostCustomRepo {
    private final JPAQueryFactory jpaQueryFactory;
    private final QPostEntity qPostEntity = QPostEntity.postEntity;

    @Override
    public Page<PostVO> searchPostList(Pageable pageable, String keyword) {
        List<PostVO> postVOS = jpaQueryFactory
                .select(Projections.constructor(PostVO.class,
                        qPostEntity.id,
                        qPostEntity.title,
                        qPostEntity.content,
                        formattedDate(qPostEntity.updatedAt),
                        qPostEntity.author.nickname,
                        qPostEntity.thumbnail,
                        qPostEntity.createdAt.eq(qPostEntity.updatedAt)
                        ))
                .from(qPostEntity)
                .where(qPostEntity.title.contains(keyword).or(qPostEntity.content.contains(keyword)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(postVOS, pageable, postVOS.size()) ;
    }

    private StringTemplate formattedDate(DateTimePath<LocalDateTime> createdAt) {
        return Expressions.stringTemplate("DATE_FORMAT({0}, {1})",
                createdAt,
                ConstantImpl.create("%Y-%m-%d"));
    }
}
