package xyz.jiwook.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.jiwook.board.dao.MemberRepo;
import xyz.jiwook.board.dao.PostRepo;
import xyz.jiwook.board.entity.PostEntity;
import xyz.jiwook.board.vo.PostVO;

@RequiredArgsConstructor
@Service
public class PostService {
    private final MemberRepo memberRepo;
    private final PostRepo postRepo;
    public long createPost(PostVO postVO) {
        return postRepo.save(PostEntity.builder()
                .title(postVO.getTitle())
                .content(postVO.getContent())
                .author(memberRepo.findByUsername(postVO.getAuthor()).orElseThrow(() -> new UsernameNotFoundException(postVO.getAuthor())))
                .thumbnail(postVO.getThumbnail())
                .build()).getId();
    }
}
