package xyz.jiwook.board.domain.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.jiwook.board.domain.board.model.BoardVO;
import xyz.jiwook.board.domain.board.service.BoardService;
import xyz.jiwook.board.global.common.model.CommonDTO;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/board")
@RestController
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<?> postBoard(@RequestBody BoardVO boardVO, Principal principal) {
        boardVO.setAuthor(principal.getName());
        CommonDTO commonDTO = boardService.createBoard(boardVO);
        if (commonDTO.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(commonDTO.getData());
        } else {
            return ResponseEntity.ok(commonDTO.getMessage());
        }
    }
}
