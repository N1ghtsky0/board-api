package xyz.jiwook.board.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import xyz.jiwook.board.vo.PostVO;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest extends CommonTestController{
    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser
    @DisplayName("게시글 작성")
    void createPost() throws Exception {
        //given
        final String URI = "/posts";
        PostVO postVO = new PostVO();
        postVO.setTitle("mock title");
        postVO.setContent("mock content");
        final String ACCESS_TOKEN = "test token";

        //when
        ResultActions result = mvc.perform(post(URI)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postVO)))
                .andDo(print());

        //then
        result.andExpect(status().isCreated());
        JsonNode jsonNode = objectMapper.readTree(result.andReturn().getResponse().getContentAsString());
        assertTrue(jsonNode.path("success").asBoolean());
    }

    @Test
    @DisplayName("게시글 목록 조회")
    void getPosts() throws Exception{
        //given
        final String URI = "/posts";
        final int page = 0;
        final int size = 20;
        final String keyword = "";

        //when
        ResultActions result = mvc.perform(get(URI)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("keywork", keyword))
                .andDo(print());

        //then
        result.andExpect(status().isOk());
        JsonNode jsonNode = objectMapper.readTree(result.andReturn().getResponse().getContentAsString());
        assertTrue(jsonNode.path("success").asBoolean());
        assertTrue(jsonNode.path("data").path("content").isArray());
    }

    @Test
    @WithMockUser
    @DisplayName("게시글 업데이트")
    void updatePost() throws Exception {
        //given
        final String URI = "/posts/1";
        PostVO postVO = new PostVO();
        postVO.setTitle("mock title");
        postVO.setContent("mock content");
        final String ACCESS_TOKEN = "test token";

        //when
        ResultActions result = mvc.perform(post(URI)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postVO)))
                .andDo(print());

        //then
        result.andExpect(status().isOk());
        JsonNode jsonNode = objectMapper.readTree(result.andReturn().getResponse().getContentAsString());
        assertTrue(jsonNode.path("success").asBoolean());
    }

    @Test
    @DisplayName("게시글 조회")
    void getPost() throws Exception {
        //given
        final String URI = "/posts/1";

        //when
        ResultActions result = mvc.perform(get(URI))
                .andDo(print());

        //then
        result.andExpect(status().isOk());
        JsonNode jsonNode = objectMapper.readTree(result.andReturn().getResponse().getContentAsString());
        assertTrue(jsonNode.path("success").asBoolean());
        assertFalse(jsonNode.path("data").path("title").asText().isEmpty());
    }

    @Test
    @WithMockUser
    @DisplayName("게시글 삭제")
    void deletePost() throws Exception {
        //given
        final String URI = "/posts/1";
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