package xyz.jiwook.board.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.jiwook.board.vo.CommentVO;
import xyz.jiwook.board.vo.ResponseVO;

@RestController
public class CommentController {
    @PostMapping("/posts/{id}/comment")
    public ResponseEntity<ResponseVO> createComment(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseVO.success());
    }

    @PostMapping("/comments/{id}")
    public ResponseEntity<ResponseVO> updateComment(@PathVariable("id") Long id, @RequestBody CommentVO commentVO) {
        return ResponseEntity.ok(ResponseVO.success());
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ResponseVO> deleteComment(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ResponseVO.success());
    }
}
