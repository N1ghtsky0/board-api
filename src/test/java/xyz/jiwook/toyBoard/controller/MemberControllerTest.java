package xyz.jiwook.toyBoard.controller;

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
import xyz.jiwook.toyBoard.vo.request.RegisterVO;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(@Autowired AccountRepo accountRepo) {
        accountRepo.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void register_success() throws Exception {
        // given
        String uri = "/member/register";
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
        String uri = "/member/register";
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
        String uri = "/member/register";
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
}