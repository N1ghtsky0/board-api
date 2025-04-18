package xyz.jiwook.board.dao;

import xyz.jiwook.board.vo.UserInfoVO;

public interface MemberCustomRepo {
    UserInfoVO selectUserInfoByUsername(String username);
}
