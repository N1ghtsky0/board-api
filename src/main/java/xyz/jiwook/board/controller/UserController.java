package xyz.jiwook.board.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.jiwook.board.vo.ResponseVO;
import xyz.jiwook.board.vo.TokenVO;
import xyz.jiwook.board.vo.UserInfoVO;
import xyz.jiwook.board.vo.UsernamePasswordVO;

@RestController
public class UserController {
    @PostMapping("/user/register")
    public ResponseEntity<ResponseVO> register(@RequestBody UsernamePasswordVO usernamePasswordVO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseVO.success());
    }

    @PostMapping("/user/login")
    public ResponseEntity<ResponseVO> login(@RequestBody UsernamePasswordVO usernamePasswordVO) {
        TokenVO tokenVO = new TokenVO();
        tokenVO.setAccessToken("mock accessToken");
        tokenVO.setRefreshToken("mock refreshToken");
        return ResponseEntity.status(HttpStatus.OK).body(ResponseVO.success(tokenVO));
    }

    @PostMapping("/user/logout")
    public ResponseEntity<ResponseVO> logout() {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseVO.success());
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseVO> myInfo() {
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setKickName("mock kickName");
        userInfoVO.setThumbnail("mock thumbnail");
        userInfoVO.setIntroduce("mock introduce");
        userInfoVO.setRegDt("mock regDt");
        return ResponseEntity.status(HttpStatus.OK).body(ResponseVO.success(userInfoVO));
    }

    @PostMapping("/users/{id}/follow")
    public ResponseEntity<ResponseVO> followUser(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseVO.success());
    }

    @DeleteMapping("/users/{id}/follow")
    public ResponseEntity<ResponseVO> unfollowUser(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseVO.success());
    }

    @PostMapping("/users/{id}/report")
    public ResponseEntity<ResponseVO> reportUser(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseVO.success());
    }
}
