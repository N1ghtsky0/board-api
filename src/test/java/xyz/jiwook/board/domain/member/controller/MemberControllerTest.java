package xyz.jiwook.board.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import xyz.jiwook.board.domain.member.model.LoginVO;
import xyz.jiwook.board.domain.member.model.MemberEntity;
import xyz.jiwook.board.domain.member.repository.MemberCRUDRepo;
import xyz.jiwook.board.domain.member.service.MemberService;
import xyz.jiwook.board.global.seucirtyConfig.SecurityConfig;

import java.util.HashMap;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberCRUDRepo memberCRUDRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clearDatabase() {
        memberCRUDRepo.deleteAll();
    }

    @DisplayName("회원가입 성공")
    @Test
    void memberRegisterSuccess() throws Exception {
        //given
        final String url = "/user/register";
        LoginVO loginVO = new LoginVO("username", "password");

        //when
        ResultActions result = mvc.perform(post(url)
                .content(objectMapper.writeValueAsString(loginVO))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
        MemberEntity registeredMember = memberCRUDRepo.findByUsername(loginVO.getUsername()).orElse(null);

        //then
        result.andExpect(status().isOk());
        assertNotNull(registeredMember);
    }

    @DisplayName("아이디 중복으로 인한 회원가입 실패")
    @Test
    void memberRegisterFailCausedUsernameDuplicate() throws Exception {
        //given
        final String url = "/user/register";
        LoginVO loginVO = new LoginVO("username", "password");
        LoginVO loginVO2 = new LoginVO("username", "password");
        mvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(loginVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //when
        ResultActions result = mvc.perform(post(url)
                .content(objectMapper.writeValueAsString(loginVO2))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
        MemberEntity registeredMember = memberCRUDRepo.findByUsername(loginVO.getUsername()).orElse(null);

        //then
        result.andExpect(status().isBadRequest());
        assertEquals(1, StreamSupport.stream(memberCRUDRepo.findAll().spliterator(), false).count());
        assertNotNull(registeredMember);
    }

    @DisplayName("로그인 성공")
    @Test
    void loginSuccess() throws Exception {
        //given
        final String url = "/user/login";
        LoginVO loginVO = new LoginVO("username", "password");
        memberService.createMember(loginVO);

        //when
        ResultActions result = mvc.perform(post(url)
                .content(objectMapper.writeValueAsString(loginVO))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        result.andExpect(status().isOk());
        String ResponseBody = result.andReturn().getResponse().getContentAsString();
        assertFalse(ResponseBody.isEmpty());
        HashMap<String, String> resultData = objectMapper.readValue(ResponseBody, HashMap.class);
        assertEquals("true", String.valueOf(resultData.get("success")));
    }

    @DisplayName("존재하지 않는 아이디로 인한 로그인 실패")
    @Test
    void loginFailCausedNotExistUsername() throws Exception {
        //given
        final String url = "/user/login";
        LoginVO loginVO = new LoginVO("username", "password");
        memberService.createMember(loginVO);
        LoginVO loginVO2 = new LoginVO("username2", "password");

        //when
        ResultActions result = mvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(loginVO2))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        result.andExpect(status().isOk());
        String ResponseBody = result.andReturn().getResponse().getContentAsString();
        assertTrue(ResponseBody.isEmpty());
    }

    @DisplayName("틀린 비밀번호로 인한 로그인 실패")
    @Test
    void loginFailCausedWrongPassword() throws Exception {
        //given
        final String url = "/user/login";
        LoginVO loginVO = new LoginVO("username", "password");
        memberService.createMember(loginVO);
        LoginVO loginVO2 = new LoginVO("username", "password2");

        //when
        ResultActions result = mvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(loginVO2))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        result.andExpect(status().isOk());
        String ResponseBody = result.andReturn().getResponse().getContentAsString();
        assertTrue(ResponseBody.isEmpty());
    }

}