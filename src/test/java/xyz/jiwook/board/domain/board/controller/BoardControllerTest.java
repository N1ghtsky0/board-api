package xyz.jiwook.board.domain.board.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import xyz.jiwook.board.domain.board.model.BoardEntity;
import xyz.jiwook.board.domain.board.model.BoardVO;
import xyz.jiwook.board.domain.board.repository.BoardCRUDRepo;
import xyz.jiwook.board.domain.member.model.LoginVO;
import xyz.jiwook.board.domain.member.repository.MemberCRUDRepo;
import xyz.jiwook.board.domain.member.service.MemberService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest
class BoardControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberCRUDRepo memberCRUDRepo;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BoardCRUDRepo boardCRUDRepo;

    private final LoginVO loginVO = new LoginVO("username", "password");
    private String accessToken;

    @BeforeEach
    void setUp() throws Exception {
        boardCRUDRepo.deleteAll();
        memberCRUDRepo.deleteAll();
        memberService.createMember(loginVO);

        String responseBodyText = mvc.perform(post("/user/login")
                        .content(objectMapper.writeValueAsString(loginVO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(responseBodyText);
        accessToken = rootNode.path("data").path("accessToken").asText();
    }

    @DisplayName("글 작성 성공")
    @Transactional
    @WithMockUser(username = "username")
    @Test
    void postBoardSuccess() throws Exception {
        //given
        final String url = "/board";
        BoardVO boardVO = new BoardVO();
        boardVO.setTitle("title");
        boardVO.setContent("content");

        //when
        ResultActions result = mvc.perform(post(url)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardVO)))
                .andDo(print());

        //then
        result.andExpect(status().isCreated());
        BoardEntity savedBoard = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), BoardEntity.class);
        assertEquals(boardVO.getTitle(), savedBoard.getTitle());
        assertEquals(boardVO.getContent(), savedBoard.getContent());
        assertEquals(loginVO.getUsername(), savedBoard.getMember().getUsername());
    }

}