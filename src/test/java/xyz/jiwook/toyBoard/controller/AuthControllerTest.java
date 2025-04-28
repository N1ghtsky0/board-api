package xyz.jiwook.toyBoard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import xyz.jiwook.toyBoard.dao.AccountRepo;
import xyz.jiwook.toyBoard.dao.RefreshTokenRepo;
import xyz.jiwook.toyBoard.entity.RefreshTokenEntity;
import xyz.jiwook.toyBoard.service.MemberService;
import xyz.jiwook.toyBoard.vo.request.LoginVO;
import xyz.jiwook.toyBoard.vo.request.RegisterVO;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @BeforeEach
    public void setUp() {
        accountRepo.deleteAll();
    }

    @Test
    @DisplayName("유저 로그인 성공")
    public void login_success() throws Exception {
        // given
        String uri = "/auth/login";
        RegisterVO registerVO = new RegisterVO();
        registerVO.setUsername("username");
        registerVO.setPassword("password");
        memberService.register(registerVO);

        LoginVO loginVO = new LoginVO();
        loginVO.setUsername("username");
        loginVO.setPassword("password");

        // when
        ResultActions result = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginVO)))
                .andDo(print());

        // then
        result.andExpect(status().isOk());
        assertNotNull(result.andReturn().getResponse().getHeader("Authorization"));
        assertNotNull(result.andReturn().getResponse().getCookie("refresh-token"));
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void reGenerateToken_success() throws Exception {
        // given
        String uri = "/auth/token/refresh";
        String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTc0NTU0NzkzMSwiZXhwIjoxNzQ1NTQ3OTMxfQ.2u6bCJIvmLTjzMH9hzPKCNU8VCVNPIixAFA0zp1_CKsJ7huv0_J6KrLoBJo-HBX69iTmpUeweS_4XyeBsESr0Q";
        String refreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyZWZyZXNoVG9rZW4iLCJpYXQiOjE3NDU1NDM1NTcsImV4cCI6MTc0NjE0ODM1N30.p5QP5zK2uL-hrcc1qwVRazX23kAdtlQ-b8MdwPXdOhaBJjFVhi4AkB19ynbv3RKDHfcIouzJbz2Wo_SpUlkySg";
        refreshTokenRepo.save(new RefreshTokenEntity(refreshToken, "127.0.0.1", "username"));

        Cookie refreshTokenCookie = new Cookie("refresh-token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");

        // when
        ResultActions result = mockMvc.perform(post(uri)
                        .header("Authorization", "Bearer " + accessToken)
                        .cookie(refreshTokenCookie))
                .andDo(print());

        // then
        result.andExpect(status().isOk());
        assertNotNull(result.andReturn().getResponse().getHeader("Authorization"));
        assertNotEquals(accessToken, result.andReturn().getResponse().getHeader("Authorization"));
    }
}