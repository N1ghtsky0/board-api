package xyz.jiwook.toyBoard.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.jiwook.toyBoard.service.PostService;
import xyz.jiwook.toyBoard.util.HttpContextUtils;
import xyz.jiwook.toyBoard.vo.reponse.PostDetailVO;
import xyz.jiwook.toyBoard.vo.reponse.PostSummaryVO;
import xyz.jiwook.toyBoard.vo.request.EditPostVO;

import java.time.Duration;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;
    private final HttpContextUtils httpContextUtils;
    private final RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/posts")
    public ResponseEntity<Void> createPost(@RequestBody @Valid EditPostVO createPostVO) {
        postService.createPost(createPostVO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts")
    public ResponseEntity<Page<PostSummaryVO>> getPostList(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "searchType", required = false, defaultValue = "T") String searchType,
            @RequestParam(value = "searchKeyword", required = false, defaultValue = "") String searchKeyword) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(postService.searchPostList(pageRequest, searchType, searchKeyword));
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDetailVO> getPost(@PathVariable("id") long postId,
                                                HttpServletRequest request) {
        return ResponseEntity.ok(postService.getPost(postId, increaseViewCount(postId, httpContextUtils.extractVisitorId(request))));
    }

    @PostMapping("/posts/{id}")
    public ResponseEntity<Void> updatePost(@PathVariable("id") long postId,
                                           @RequestBody @Valid EditPostVO editPostVO) {
        editPostVO.setId(postId);
        postService.updatePost(editPostVO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    private boolean increaseViewCount(long postId, String visitorId) {
        String redisKey = "views:" + visitorId;
        String postIdStr = String.valueOf(postId);
        BoundSetOperations<String, Object> setOps = redisTemplate.boundSetOps(redisKey);
        if (Boolean.TRUE.equals(setOps.isMember(postIdStr))) {
            return false;
        }
        setOps.add(postIdStr);
        redisTemplate.expire(redisKey, Duration.ofDays(7));
        return true;
    }
}
