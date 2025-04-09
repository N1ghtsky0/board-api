package xyz.jiwook.board.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xyz.jiwook.board.domain.board.model.BoardEntity;
import xyz.jiwook.board.domain.board.model.BoardVO;
import xyz.jiwook.board.domain.board.repository.BoardCRUDRepo;
import xyz.jiwook.board.domain.board.repository.BoardDao;
import xyz.jiwook.board.domain.member.model.MemberEntity;
import xyz.jiwook.board.domain.member.repository.MemberCRUDRepo;
import xyz.jiwook.board.global.common.model.CommonDTO;
import xyz.jiwook.board.global.common.model.SearchVO;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardCRUDRepo boardCRUDRepo;
    private final BoardDao boardDao;
    private final MemberCRUDRepo memberCRUDRepo;

    public CommonDTO createBoard(BoardVO boardVO) {
        MemberEntity author = memberCRUDRepo.findByUsername(boardVO.getAuthor()).orElse(null);
        if (author == null) { return CommonDTO.fail("username not exist"); }
        BoardEntity boardEntity = boardCRUDRepo.save(BoardEntity.builder()
                .title(boardVO.getTitle())
                .content(boardVO.getContent())
                .member(author)
                .build());
        return CommonDTO.success(boardEntity);
    }

    public Page<BoardVO> searchBoardList(SearchVO searchVO, Pageable pageable) {
        return boardDao.searchBoardList(searchVO, pageable);
    }
}
