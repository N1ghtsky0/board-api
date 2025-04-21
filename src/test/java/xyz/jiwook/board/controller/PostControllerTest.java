package xyz.jiwook.board.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import xyz.jiwook.board.service.PostService;
import xyz.jiwook.board.vo.PostVO;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PostControllerTest extends CommonTestController {
    @Autowired
    private PostService postService;

    @Test
    @DisplayName("게시글 작성")
    void createPost() throws Exception {
        //given
        final String URI = "/posts";
        PostVO postVO = new PostVO();
        postVO.setTitle("mock title");
        postVO.setContent("mock content");

        //when
        ResultActions result = mvc.perform(post(URI)
                        .header("Authorization", "Bearer " + USABLE_ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postVO)))
                .andDo(print());

        //then
        result.andExpect(status().isCreated());
        JsonNode jsonNode = objectMapper.readTree(result.andReturn().getResponse().getContentAsString());
        assertTrue(jsonNode.path("success").asBoolean());
        assertEquals(postVO.getTitle(), jsonNode.path("data").path("title").asText());
        assertEquals(postVO.getContent(), jsonNode.path("data").path("content").asText());
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
        PostVO postVO = new PostVO();
        postVO.setAuthor(TEST_USERNAME);
        postVO.setTitle("mock title");
        postVO.setContent("mock content");
        long postSeq = postService.createPost(postVO);

        final String URI = "/posts/" + postSeq;
        PostVO updatedPostVO = new PostVO();
        updatedPostVO.setTitle("updated mock title");
        updatedPostVO.setContent("updated mock content");

        //when
        ResultActions result = mvc.perform(post(URI)
                        .header("Authorization", "Bearer " + USABLE_ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPostVO)))
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