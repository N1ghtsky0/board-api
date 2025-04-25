package xyz.jiwook.toyBoard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import xyz.jiwook.toyBoard.dao.PostRepo;
import xyz.jiwook.toyBoard.service.MemberService;
import xyz.jiwook.toyBoard.vo.request.EditPostVO;
import xyz.jiwook.toyBoard.vo.request.RegisterVO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PostRepo postRepo;

    private final String ACCESS_TOKEN = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTc0NTU2MTQzMCwiZXhwIjo0MTAyNDEyMzk5fQ.Cb8Uq3twVm5nm_AElyjgeWNHNmu2B8j_ZhMyP8BYKCBd1xRBlYFx52uZI3IZKQDlHvM8qfrU6XgHU5QHthFBsQ";

    @BeforeAll
    public static void accountSetUp(@Autowired MemberService memberService) {
        RegisterVO registerVO = new RegisterVO();
        registerVO.setUsername("username");
        registerVO.setPassword("password");
        memberService.register(registerVO);
    }

    @BeforeEach
    public void setUp() {
        postRepo.deleteAll();
    }

    @Test
    @DisplayName("게시글 작성 성공")
    void createPost_success() throws Exception {
        // given
        String uri = "/posts";
        EditPostVO createPostVO = new EditPostVO();
        createPostVO.setTitle("title");
        createPostVO.setContent("content");

        // when
        ResultActions result = mockMvc.perform(post(uri)
                .header("Authorization", ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createPostVO)))
                .andDo(print());

        // then
        result.andExpect(status().isOk());
    }

}