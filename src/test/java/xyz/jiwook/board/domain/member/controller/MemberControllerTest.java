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
import xyz.jiwook.board.global.seucirtyConfig.SecurityConfig;

import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {
    @Autowired
    private MockMvc mvc;

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
                .contentType(MediaType.APPLICATION_JSON));
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
                .contentType(MediaType.APPLICATION_JSON));
        MemberEntity registeredMember = memberCRUDRepo.findByUsername(loginVO.getUsername()).orElse(null);

        //then
        result.andExpect(status().isBadRequest());
        assertEquals(1, StreamSupport.stream(memberCRUDRepo.findAll().spliterator(), false).count());
        assertNotNull(registeredMember);
    }

}