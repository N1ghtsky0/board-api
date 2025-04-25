package xyz.jiwook.toyBoard.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import xyz.jiwook.toyBoard.vo.request.TokenVO;

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
    @DisplayName("회원가입 성공")
    void register_success() throws Exception {
        // given
        String uri = "/auth/member/register";
        RegisterVO registerVO = new RegisterVO();
        registerVO.setUsername("username");
        registerVO.setPassword("password");

        // when
        ResultActions result = mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerVO)))
                .andDo(print());

        // then
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 실패 - 아이디 검증")
    void register_fail_usernameValid() throws Exception {
        // case1
        // given
        String uri = "/auth/member/register";
        RegisterVO registerVO = new RegisterVO();
        registerVO.setUsername("");
        registerVO.setPassword("password");

        // when
        ResultActions result = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerVO)))
                .andDo(print());

        // then
        result.andExpect(status().isBadRequest());
        assertEquals("아이디는 필수입니다.", result.andReturn().getResponse().getContentAsString());

        // case2
        // given
        registerVO.setUsername("user name");

        // when
        result = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerVO)))
                .andDo(print());

        // then
        result.andExpect(status().isBadRequest());
        assertEquals("아이디에는 공백이 포함될 수 없습니다.", result.andReturn().getResponse().getContentAsString());

        // case3
        // given
        registerVO.setUsername("user");

        // when
        result = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerVO)))
                .andDo(print());

        // then
        result.andExpect(status().isBadRequest());
        assertEquals("아이디는 8자 이상 20자 이하로 입력하세요.", result.andReturn().getResponse().getContentAsString());

        // case4
        // given
        registerVO.setUsername("username_username_username");

        // when
        result = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerVO)))
                .andDo(print());

        // then
        result.andExpect(status().isBadRequest());
        assertEquals("아이디는 8자 이상 20자 이하로 입력하세요.", result.andReturn().getResponse().getContentAsString());
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 검증")
    void register_fail_passwordValid() throws Exception {
        // case1
        // given
        String uri = "/auth/member/register";
        RegisterVO registerVO = new RegisterVO();
        registerVO.setUsername("username");
        registerVO.setPassword("");

        // when
        ResultActions result = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerVO)))
                .andDo(print());

        // then
        result.andExpect(status().isBadRequest());
        assertEquals("비밀번호는 필수입니다.", result.andReturn().getResponse().getContentAsString());

        // case2
        // given
        registerVO.setPassword("pass word");

        // when
        result = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerVO)))
                .andDo(print());

        // then
        result.andExpect(status().isBadRequest());
        assertEquals("비밀번호에는 공백이 포함될 수 없습니다.", result.andReturn().getResponse().getContentAsString());

        // case3
        // given
        registerVO.setPassword("pwd");

        // when
        result = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerVO)))
                .andDo(print());

        // then
        result.andExpect(status().isBadRequest());
        assertEquals("비밀번호는 8자 이상 20자 이하로 입력하세요.", result.andReturn().getResponse().getContentAsString());

        // case4
        // given
        registerVO.setPassword("password_password_password");

        // when
        result = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerVO)))
                .andDo(print());

        // then
        result.andExpect(status().isBadRequest());
        assertEquals("비밀번호는 8자 이상 20자 이하로 입력하세요.", result.andReturn().getResponse().getContentAsString());
    }

    @Test
    @DisplayName("로그인 성공")
    public void login_success() throws Exception {
        // given
        String uri = "/auth/member/login";
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
        JsonNode jsonNode = objectMapper.readTree(result.andReturn().getResponse().getContentAsString());
        assertTrue(jsonNode.has("accessToken") && jsonNode.has("refreshToken"));
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void reGenerateToken_success() throws Exception {
        // given
        String uri = "/auth/token/refresh";
        String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTc0NTU0NzkzMSwiZXhwIjoxNzQ1NTQ3OTMxfQ.2u6bCJIvmLTjzMH9hzPKCNU8VCVNPIixAFA0zp1_CKsJ7huv0_J6KrLoBJo-HBX69iTmpUeweS_4XyeBsESr0Q";
        String refreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyZWZyZXNoVG9rZW4iLCJpYXQiOjE3NDU1NDM1NTcsImV4cCI6MTc0NjE0ODM1N30.p5QP5zK2uL-hrcc1qwVRazX23kAdtlQ-b8MdwPXdOhaBJjFVhi4AkB19ynbv3RKDHfcIouzJbz2Wo_SpUlkySg";
        TokenVO requestTokenVO = new TokenVO();
        requestTokenVO.setAccessToken(accessToken);
        requestTokenVO.setRefreshToken(refreshToken);
        refreshTokenRepo.save(new RefreshTokenEntity(refreshToken, "127.0.0.1", "username"));

        // when
        ResultActions result = mockMvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestTokenVO)))
                .andDo(print());

        // then
        result.andExpect(status().isOk());
        JsonNode jsonNode = objectMapper.readTree(result.andReturn().getResponse().getContentAsString());
        assertTrue(jsonNode.has("accessToken") && jsonNode.has("refreshToken"));
        assertFalse(jsonNode.path("accessToken").asText().isEmpty());
    }
}