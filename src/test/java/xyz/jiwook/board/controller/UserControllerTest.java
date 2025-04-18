package xyz.jiwook.board.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import xyz.jiwook.board.vo.UsernamePasswordVO;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserControllerTest extends CommonTestController{
    @Test
    @DisplayName("회원가입")
    void register() throws Exception {
        // given
        final String URI = "/user/register";
        UsernamePasswordVO usernamePasswordVO = new UsernamePasswordVO();
        usernamePasswordVO.setUsername("mock username");
        usernamePasswordVO.setPassword("mock password");

        // when
        ResultActions result = mvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usernamePasswordVO)))
                .andDo(print());

        // then
        result.andExpect(status().isCreated());
        JsonNode jsonNode = objectMapper.readTree(result.andReturn().getResponse().getContentAsString());
        assertTrue(jsonNode.path("success").asBoolean());
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        //given
        final String URI = "/user/login";
        UsernamePasswordVO usernamePasswordVO = new UsernamePasswordVO();
        usernamePasswordVO.setUsername("mock username");
        usernamePasswordVO.setPassword("mock password");

        //when
        ResultActions result = mvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usernamePasswordVO)))
                .andDo(print());

        //then
        result.andExpect(status().isOk());
        JsonNode jsonNode = objectMapper.readTree(result.andReturn().getResponse().getContentAsString());
        assertTrue(jsonNode.path("success").asBoolean());
        assertFalse(jsonNode.path("data").path("accessToken").asText().isEmpty());
        assertFalse(jsonNode.path("data").path("refreshToken").asText().isEmpty());
    }

    @Test
    @WithMockUser
    @DisplayName("로그 아웃")
    void logout() throws Exception {
        //given
        final String URI = "/user/logout";
        final String ACCESS_TOKEN = "test token";

        //when
        ResultActions result = mvc.perform(post(URI)
                .header("Authorization", "Bearer " + ACCESS_TOKEN));

        //then
        result.andExpect(status().isOk());
        JsonNode jsonNode = objectMapper.readTree(result.andReturn().getResponse().getContentAsString());
        assertTrue(jsonNode.path("success").asBoolean());
    }

    @Test
    @WithMockUser
    @DisplayName("내 정보 조회")
    void getMyInfo() throws Exception {
        //given
        final String URI = "/me";
        final String ACCESS_TOKEN = "test token";

        //when
        ResultActions result = mvc.perform(get(URI)
                .header("Authorization", "Bearer " + ACCESS_TOKEN));

        //then
        result.andExpect(status().isOk());
        JsonNode jsonNode = objectMapper.readTree(result.andReturn().getResponse().getContentAsString());
        assertTrue(jsonNode.path("success").asBoolean());
        assertFalse(jsonNode.path("data").path("kickName").asText().isEmpty());
        assertFalse(jsonNode.path("data").path("thumbnail").asText().isEmpty());
        assertFalse(jsonNode.path("data").path("regDt").asText().isEmpty());
    }
}
