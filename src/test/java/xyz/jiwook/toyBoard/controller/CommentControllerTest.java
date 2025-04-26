package xyz.jiwook.toyBoard.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import xyz.jiwook.toyBoard.config.SecurityConfig.UserDetailsServiceImpl;
import xyz.jiwook.toyBoard.dao.CommentRepo;
import xyz.jiwook.toyBoard.dao.MemberRepo;
import xyz.jiwook.toyBoard.dao.PostRepo;
import xyz.jiwook.toyBoard.entity.BaseAccountEntity;
import xyz.jiwook.toyBoard.entity.PostEntity;
import xyz.jiwook.toyBoard.service.MemberService;
import xyz.jiwook.toyBoard.vo.request.ContentOnly;
import xyz.jiwook.toyBoard.vo.request.RegisterVO;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentRepo commentRepo;

    private final String ACCESS_TOKEN = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTc0NTU2MTQzMCwiZXhwIjo0MTAyNDEyMzk5fQ.Cb8Uq3twVm5nm_AElyjgeWNHNmu2B8j_ZhMyP8BYKCBd1xRBlYFx52uZI3IZKQDlHvM8qfrU6XgHU5QHthFBsQ";
    private static long POST_SEQ;

    @BeforeAll
    public static void accountSetUp(@Autowired MemberService memberService,
                                    @Autowired MemberRepo memberRepo,
                                    @Autowired PostRepo postRepo,
                                    @Autowired UserDetailsServiceImpl userDetailsService) {
        RegisterVO registerVO = new RegisterVO();
        registerVO.setUsername("username");
        registerVO.setPassword("password");
        memberService.register(registerVO);

        registerVO.setUsername("username2");
        registerVO.setPassword("password2");
        memberService.register(registerVO);

        BaseAccountEntity loginAccount = (BaseAccountEntity) userDetailsService.loadUserByUsername("username");
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginAccount, null, loginAccount.getAuthorities());
        authentication.setDetails(loginAccount);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        POST_SEQ = postRepo.save(PostEntity.builder()
                        .title("title")
                        .content("content")
                        .author(Objects.requireNonNull(memberRepo.findByUsername("username").orElse(null)))
                        .build()).getId();
    }

    @BeforeEach
    public void setUp() {
        commentRepo.deleteAll();
    }

    @Test
    @DisplayName("댓글 작성 성공")
    void createComment_success() throws Exception {
        // given
        String uri = "/posts/" + POST_SEQ + "/comments";
        ContentOnly requestBody = new ContentOnly("commentContent");

        // when
        ResultActions result = mockMvc.perform(post(uri)
                        .header("Authorization", ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andDo(print());

        // then
        result.andExpect(status().isOk());
        JsonNode jsonNode = objectMapper.readTree(result.andReturn().getResponse().getContentAsString());
        assertEquals(requestBody.content(), jsonNode.path("content").asText());
        assertEquals("username", jsonNode.path("author").asText());
    }

    @Test
    @DisplayName("댓글 작성 실패 - 없는 게시물")
    void createComment_fail_postNotFound() throws Exception {
        // given
        String uri = "/posts/0/comments";
        ContentOnly requestBody = new ContentOnly("commentContent");

        // when
        ResultActions result = mockMvc.perform(post(uri)
                        .header("Authorization", ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andDo(print());

        // then
        result.andExpect(status().isBadRequest());
    }
}