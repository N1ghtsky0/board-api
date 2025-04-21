package xyz.jiwook.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jiwook.board.dao.MemberRepo;
import xyz.jiwook.board.dao.PostRepo;
import xyz.jiwook.board.entity.MemberEntity;
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

    @Transactional(readOnly = true)
    public PageImpl<PostVO> searchPosts(Pageable pageable, String keyword) {
        return (PageImpl<PostVO>) postRepo.searchPostList(pageable, keyword);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updatePost(PostVO postVO, MemberEntity loginUser) {
        PostEntity postEntity = postRepo.findById(postVO.getId()).orElse(null);
        if (postEntity == null || !postEntity.getAuthor().getId().equals(loginUser.getId())) {
            throw new AccessDeniedException("글을 수정할 권한이 없습니다.");
        }
        postEntity.updatePost(postVO);
        postRepo.save(postEntity);
    }
}
