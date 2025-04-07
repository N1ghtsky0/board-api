package xyz.jiwook.board.domain.member.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import xyz.jiwook.board.domain.member.model.LoginVO;
import xyz.jiwook.board.domain.member.model.MemberEntity;
import xyz.jiwook.board.domain.member.repository.MemberCRUDRepo;

import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {
    @Autowired
    private MemberCRUDRepo memberCRUDRepo;

    @Autowired
    private MemberService memberService;

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

}