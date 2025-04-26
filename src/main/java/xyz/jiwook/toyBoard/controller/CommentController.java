package xyz.jiwook.toyBoard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.jiwook.toyBoard.service.CommentService;
import xyz.jiwook.toyBoard.vo.reponse.CommentVO;
import xyz.jiwook.toyBoard.vo.request.ContentOnly;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/posts/{id}/comments")
    public ResponseEntity<CommentVO> createComment(@PathVariable("id") long postId,
                                                   @RequestBody ContentOnly contentOnly) {
        return ResponseEntity.ok(commentService.createComment(contentOnly.content(), postId));
    }

    @GetMapping("/posts/{id}/comments")
    public ResponseEntity<List<CommentVO>> getCommentList(@PathVariable("id") long postId) {
        return ResponseEntity.ok(commentService.getCommentList(postId));
    }
}
