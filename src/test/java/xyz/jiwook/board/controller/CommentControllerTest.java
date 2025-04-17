package xyz.jiwook.board.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import xyz.jiwook.board.vo.CommentVO;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest extends CommonTestController {
    @Test
    @WithMockUser
    @DisplayName("댓글 작성")
    void createComment() throws Exception {
        //given
        final String URI = "/posts/1/comment";
        CommentVO commentVO = new CommentVO();
        commentVO.setContent("mock content");
        final String ACCESS_TOKEN = "test token";

        //when
        ResultActions result = mvc.perform(post(URI)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentVO)))
                .andDo(print());

        //then
        result.andExpect(status().isCreated());
        JsonNode jsonNode = objectMapper.readTree(result.andReturn().getResponse().getContentAsString());
        assertTrue(jsonNode.path("success").asBoolean());
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 수정")
    void updateComment() throws Exception {
        //given
        final String URI = "/comments/1";
        CommentVO commentVO = new CommentVO();
        commentVO.setContent("mock content");
        final String ACCESS_TOKEN = "test token";

        //when
        ResultActions result = mvc.perform(post(URI)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentVO)))
                .andDo(print());

        //then
        result.andExpect(status().isOk());
        JsonNode jsonNode = objectMapper.readTree(result.andReturn().getResponse().getContentAsString());
        assertTrue(jsonNode.path("success").asBoolean());
    }

    @Test
    @WithMockUser
    @DisplayName("댓글 삭제")
    void deleteComment() throws Exception {
        //given
        final String URI = "/comments/1";
        final String ACCESS_TOKEN = "test token";

        //when
        ResultActions result = mvc.perform(delete(URI)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN))
                .andDo(print());

        //then
        result.andExpect(status().isOk());
        JsonNode jsonNode = objectMapper.readTree(result.andReturn().getResponse().getContentAsString());
        assertTrue(jsonNode.path("success").asBoolean());
    }
}