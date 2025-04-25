package xyz.jiwook.toyBoard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xyz.jiwook.toyBoard.config.SecurityConfig.SecurityUtil;
import xyz.jiwook.toyBoard.dao.PostRepo;
import xyz.jiwook.toyBoard.entity.PostEntity;
import xyz.jiwook.toyBoard.vo.reponse.PostDetailVO;
import xyz.jiwook.toyBoard.vo.reponse.PostSummaryVO;
import xyz.jiwook.toyBoard.vo.request.EditPostVO;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class PostService {
    private final SecurityUtil securityUtil;
    private final PostRepo postRepo;

    public void createPost(EditPostVO editPostVO) {
        postRepo.save(PostEntity.builder()
                .title(editPostVO.getTitle())
                .content(editPostVO.getContent())
                .author(Objects.requireNonNull(securityUtil.getCurrentUser()))
                .build());
    }

    public Page<PostSummaryVO> getPostList(Pageable pageable) {
        return postRepo.findAllByDeletedIsFalseOrderByCreatedAtDesc(pageable).map(PostSummaryVO::fromEntity);
    }

    public PostDetailVO getPost(long postId) {
        return PostDetailVO.fromEntity(postRepo.findById(postId).orElse(null));
    }

    public void updatePost(EditPostVO editPostVO) {
        PostEntity postEntity = postRepo.findById(editPostVO.getId()).orElse(null);
        if (postEntity == null || !securityUtil.hasAuthority(postEntity)) {
            throw new SecurityException("글을 수정할 권한이 없습니다.");
        }
        postEntity.update(editPostVO);
        postRepo.save(postEntity);
    }
}
