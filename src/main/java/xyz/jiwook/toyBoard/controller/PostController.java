package xyz.jiwook.toyBoard.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.jiwook.toyBoard.service.PostService;
import xyz.jiwook.toyBoard.vo.reponse.PostDetailVO;
import xyz.jiwook.toyBoard.vo.reponse.PostSummaryVO;
import xyz.jiwook.toyBoard.vo.request.EditPostVO;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<Void> createPost(@RequestBody @Valid EditPostVO createPostVO) {
        postService.createPost(createPostVO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts")
    public ResponseEntity<Page<PostSummaryVO>> getPostList(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(postService.getPostList(pageRequest));
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDetailVO> getPost(@PathVariable("id") long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }
}
