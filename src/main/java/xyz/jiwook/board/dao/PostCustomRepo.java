package xyz.jiwook.board.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jiwook.board.vo.PostVO;

public interface PostCustomRepo {
    Page<PostVO> searchPostList(Pageable pageable, String keyword);
}
