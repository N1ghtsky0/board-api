package xyz.jiwook.board.domain.member.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.junit.jupiter.Testcontainers;
import xyz.jiwook.board.domain.member.model.LoginVO;
import xyz.jiwook.board.domain.member.model.MemberEntity;
import xyz.jiwook.board.domain.member.repository.MemberCRUDRepo;
import xyz.jiwook.board.global.common.model.CommonDTO;

import java.util.HashMap;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class MemberServiceTest {
    @Autowired
    private MemberCRUDRepo memberCRUDRepo;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clearDatabase() {
        memberCRUDRepo.deleteAll();
    }

    @DisplayName("유저정보 생성 성공")
    @Test
    void createMemberSuccess() {
        //given
        LoginVO loginVO = new LoginVO("username", "password");

        //when
        memberService.createMember(loginVO);

        //then
        MemberEntity savedMember = memberCRUDRepo.findByUsername(loginVO.getUsername()).orElse(null);
        assertNotNull(savedMember);
        assertEquals(loginVO.getUsername(), savedMember.getUsername());
    }

    @DisplayName("아이디 중복으로 인한 유저정보 저장 실패")
    @Test
    void createMemberFailCausedUsernameDuplicate() {
        //given
        LoginVO loginVO = new LoginVO("username", "password");
        memberService.createMember(loginVO);
        LoginVO loginVO2 = new LoginVO("username", "password");

        //when
        assertThrows(DataIntegrityViolationException.class, () -> memberService.createMember(loginVO2));

        //then
        Iterable<MemberEntity> memberList = memberCRUDRepo.findAll();
        assertEquals(1, StreamSupport.stream(memberList.spliterator(), false).count());
        assertEquals(loginVO.getUsername(), memberList.iterator().next().getUsername());
    }

    @DisplayName("로그인프로세스 성공")
    @Test
    void loginProcessSuccess() {
        //given
        LoginVO loginVO = new LoginVO("username", "password");
        memberService.createMember(loginVO);

        //when
        CommonDTO resultDTO = memberService.loginProcess(loginVO);
        HashMap<String, String> resultData = objectMapper.convertValue(resultDTO.getData(), HashMap.class);

        //then
        assertTrue(resultDTO.isSuccess());
        assertTrue(resultData.containsKey("accessToken"));
        assertNotNull(resultData.get("accessToken"));
        assertTrue(resultData.containsKey("refreshToken"));
        assertNotNull(resultData.get("refreshToken"));
    }

    @DisplayName("존재하지 않는 아이디로 인한 로그인프로세스 실패")
    @Test
    void loginProcessFailCausedNotExistUsername() {
        //given
        LoginVO loginVO = new LoginVO("username", "password");
        memberService.createMember(loginVO);
        LoginVO loginVO2 = new LoginVO("username2", "password");

        //when
        CommonDTO resultDTO = memberService.loginProcess(loginVO2);

        //then
        assertFalse(resultDTO.isSuccess());
        assertEquals("not found username", resultDTO.getMessage());
    }

    @DisplayName("틀린 비밀번호로 인한 로그인프로세스 실패")
    @Test
    void loginProcessFailCausedWrongPassword() {
        //given
        LoginVO loginVO = new LoginVO("username", "password");
        memberService.createMember(loginVO);
        LoginVO loginVO2 = new LoginVO("username", "password2");

        //when
        CommonDTO resultDTO = memberService.loginProcess(loginVO2);

        //then
        assertFalse(resultDTO.isSuccess());
        assertEquals("wrong password", resultDTO.getMessage());
    }

}