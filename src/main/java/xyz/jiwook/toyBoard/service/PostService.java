package xyz.jiwook.toyBoard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xyz.jiwook.toyBoard.config.SecurityConfig.SecurityUtil;
import xyz.jiwook.toyBoard.config.exceptionConfig.BusinessException;
import xyz.jiwook.toyBoard.dao.PostRepo;
import xyz.jiwook.toyBoard.entity.PostEntity;
import xyz.jiwook.toyBoard.vo.reponse.PostDetailVO;
import xyz.jiwook.toyBoard.vo.reponse.PostSummaryVO;
import xyz.jiwook.toyBoard.vo.request.EditPostVO;

import java.util.ArrayList;
import java.util.Objects;

import static xyz.jiwook.toyBoard.config.exceptionConfig.ErrorCode.*;

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

    public Page<PostSummaryVO> searchPostList(Pageable pageable, String searchType, String searchKeyword) {
        if (searchKeyword.isBlank()) {
            return getPostList(pageable);
        }
        switch (searchType) {
            case "T" -> {
                return postRepo.findAllByDeletedIsFalseAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(pageable, searchKeyword).map(PostSummaryVO::fromEntity);
            }
            case "C" -> {
                return postRepo.findAllByDeletedIsFalseAndContentContainingIgnoreCaseOrderByCreatedAtDesc(pageable, searchKeyword).map(PostSummaryVO::fromEntity);
            }
            case "A" -> {
                return postRepo.findAllByDeletedIsFalseAndCreatedByContainingIgnoreCaseOrderByCreatedAtDesc(pageable, searchKeyword).map(PostSummaryVO::fromEntity);
            }
            default -> {
                return new PageImpl<>(new ArrayList<>(), pageable, 0L);
            }
        }
    }

    public Page<PostSummaryVO> getPostList(Pageable pageable) {
        return postRepo.findAllByDeletedIsFalseOrderByCreatedAtDesc(pageable).map(PostSummaryVO::fromEntity);
    }

    public PostDetailVO getPost(long postId) {
        return PostDetailVO.fromEntity(postRepo.findByIdAndDeletedIsFalse(postId).orElse(null));
    }

    public void updatePost(EditPostVO editPostVO) {
        PostEntity postEntity = postRepo.findById(editPostVO.getId()).orElse(null);
        if (postEntity == null || !securityUtil.hasAuthority(postEntity)) {
            throw new BusinessException(NO_AUTHORITY);
        }
        postEntity.update(editPostVO);
        postRepo.save(postEntity);
    }

    public void deletePost(long postId) {
        PostEntity postEntity = postRepo.findById(postId).orElse(null);
        if (postEntity == null || !securityUtil.hasAuthority(postEntity)) {
            throw new BusinessException(NO_AUTHORITY);
        }
        postEntity.delete("작성자가 직접 삭제한 글입니다.");
        postRepo.save(postEntity);
    }
}
