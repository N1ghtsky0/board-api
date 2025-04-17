package xyz.jiwook.board.controller;

import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.jiwook.board.vo.PostVO;
import xyz.jiwook.board.vo.ResponseVO;

import java.util.ArrayList;

@RestController
public class PostController {
    @PostMapping("/posts")
    public ResponseEntity<ResponseVO> createPost(@RequestBody PostVO postVO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseVO.success());
    }

    @GetMapping("/posts")
    public ResponseEntity<ResponseVO> getPosts(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                               @RequestParam(value = "size", required = false, defaultValue = "20") int size,
                                               @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) {
        PageImpl<PostVO> postVOPage = new PageImpl<>(new ArrayList<>());
        return ResponseEntity.status(HttpStatus.OK).body(ResponseVO.success(postVOPage));
    }

    @PostMapping("/posts/{id}")
    public ResponseEntity<ResponseVO> updatePost(@PathVariable("id") Long id, @RequestBody PostVO postVO) {
        return ResponseEntity.ok(ResponseVO.success());
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<ResponseVO> getPost(@PathVariable("id") Long id) {
        PostVO postVO = new PostVO();
        postVO.setId(id);
        postVO.setTitle("mock title");
        postVO.setContent("mock content");
        postVO.setUpdDt("mock updDt");
        postVO.setAuthor("mock author");
        postVO.setThumbnail("mock thumbnail");
        postVO.setLikeCount(10);
        postVO.setDislikeCount(0);
        postVO.setUpdated(false);
        return ResponseEntity.ok(ResponseVO.success(postVO));
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<ResponseVO> deletePost(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ResponseVO.success());
    }
}
