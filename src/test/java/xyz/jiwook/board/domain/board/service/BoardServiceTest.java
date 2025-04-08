package xyz.jiwook.board.domain.board.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import xyz.jiwook.board.domain.board.model.BoardVO;
import xyz.jiwook.board.domain.board.repository.BoardCRUDRepo;
import xyz.jiwook.board.domain.member.model.LoginVO;
import xyz.jiwook.board.domain.member.repository.MemberCRUDRepo;
import xyz.jiwook.board.domain.member.service.MemberService;
import xyz.jiwook.board.global.common.model.CommonDTO;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class BoardServiceTest {
    @Autowired
    private BoardCRUDRepo boardCRUDRepo;

    @Autowired
    private BoardService boardService;

    @Autowired
    private MemberCRUDRepo memberCRUDRepo;

    @Autowired
    private MemberService memberService;

    private final LoginVO loginVO = new LoginVO("username", "password");

    @BeforeEach
    void setUp() {
        boardCRUDRepo.deleteAll();
        memberCRUDRepo.deleteAll();
        memberService.createMember(loginVO);
    }

    @DisplayName("글 저장 성공")
    @Test
    void createBoardSuccess() {
        //given
        BoardVO boardVO = new BoardVO();
        boardVO.setTitle("title");
        boardVO.setContent("content");
        boardVO.setAuthor(loginVO.getUsername());

        //when
        CommonDTO resultDTO = boardService.createBoard(boardVO);

        //then
        assertNotNull(resultDTO);
        assertTrue(resultDTO.isSuccess());
        assertEquals(1, boardCRUDRepo.count());
    }

    @DisplayName("존자하지 않는 username 으로 인한 글 저장 실패")
    @Test
    void createBoardFailCausedNotExistUsername() {
        //given
        BoardVO boardVO = new BoardVO();
        boardVO.setTitle("title");
        boardVO.setContent("content");
        boardVO.setAuthor("username2");

        //when
        CommonDTO resultDTO = boardService.createBoard(boardVO);

        //then
        assertNotNull(resultDTO);
        assertFalse(resultDTO.isSuccess());
        assertEquals("username not exist", resultDTO.getMessage());
    }

}