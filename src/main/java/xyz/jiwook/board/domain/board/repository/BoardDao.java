package xyz.jiwook.board.domain.board.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import xyz.jiwook.board.domain.board.model.BoardVO;
import xyz.jiwook.board.domain.board.model.QBoardEntity;
import xyz.jiwook.board.domain.member.model.QMemberEntity;
import xyz.jiwook.board.global.common.model.SearchVO;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardDao {
    private final JPAQueryFactory queryFactory;
    private final QBoardEntity qBoard = QBoardEntity.boardEntity;
    private final QMemberEntity qMember = QMemberEntity.memberEntity;

    @Transactional(readOnly = true)
    public Page<BoardVO> searchBoardList(SearchVO searchVO, Pageable pageable) {
        List<BoardVO> boardList = queryFactory
                .select(Projections.fields(BoardVO.class, qBoard.seq, qBoard.title, qBoard.content))
                .from(qBoard)
                .innerJoin(qBoard.member, qMember)
                .where(setKeyword(searchVO))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCnt = queryFactory
                .select(qBoard.count())
                .from(qBoard)
                .innerJoin(qBoard.member, qMember)
                .where(setKeyword(searchVO))
                .fetchOne();
        totalCnt = totalCnt == null ? 0 : totalCnt;
        return new PageImpl<>(boardList, pageable, totalCnt);
    }

    private BooleanExpression setKeyword(SearchVO searchVO) {
        if (searchVO == null) { return null; }
        String keyword = searchVO.getKeyword();
        return switch (searchVO.getCategory()) {
            case "all" -> qBoard.title.containsIgnoreCase(keyword)
                    .or(qBoard.content.containsIgnoreCase(keyword))
                    .or(qMember.username.containsIgnoreCase(keyword));
            case "title" -> qBoard.title.containsIgnoreCase(keyword);
            case "content" -> qBoard.content.containsIgnoreCase(keyword);
            case "author" -> qMember.username.containsIgnoreCase(keyword);
            default -> null;
        };
    }
}
