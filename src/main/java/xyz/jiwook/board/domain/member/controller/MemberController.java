package xyz.jiwook.board.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.jiwook.board.domain.member.model.LoginVO;
import xyz.jiwook.board.domain.member.service.MemberService;
import xyz.jiwook.board.global.common.model.CommonDTO;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<?> memberRegister(@RequestBody LoginVO loginVO) {
        try {
            memberService.createMember(loginVO);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginVO loginVO) {
        CommonDTO resultDTO = memberService.loginProcess(loginVO);
        if (resultDTO.isSuccess()) {
            return ResponseEntity.ok(resultDTO);
        } else {
            log.warn(resultDTO.getMessage());
            return ResponseEntity.ok().build();
        }
    }
}
