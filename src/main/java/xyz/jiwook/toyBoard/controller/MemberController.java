package xyz.jiwook.toyBoard.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.jiwook.toyBoard.config.SecurityConfig.SecurityUtil;
import xyz.jiwook.toyBoard.service.MemberService;
import xyz.jiwook.toyBoard.service.PostService;
import xyz.jiwook.toyBoard.vo.reponse.PostSummaryVO;
import xyz.jiwook.toyBoard.vo.request.RegisterVO;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final PostService postService;
    private final MemberService memberService;
    private final SecurityUtil securityUtil;

    @PostMapping("/member/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterVO registerVO) {
        memberService.register(registerVO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/posts")
    public ResponseEntity<Page<PostSummaryVO>> getMyPostList(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        final String searchType = "A";
        final String searchKeyword = securityUtil.getCurrentUsername();
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(postService.searchPostList(pageRequest, searchType, searchKeyword));
    }
}
