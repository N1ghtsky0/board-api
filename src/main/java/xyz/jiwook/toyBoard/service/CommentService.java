package xyz.jiwook.toyBoard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.jiwook.toyBoard.config.SecurityConfig.SecurityUtil;
import xyz.jiwook.toyBoard.config.exceptionConfig.BusinessException;
import xyz.jiwook.toyBoard.config.exceptionConfig.ErrorCode;
import xyz.jiwook.toyBoard.dao.CommentRepo;
import xyz.jiwook.toyBoard.dao.PostRepo;
import xyz.jiwook.toyBoard.entity.CommentEntity;
import xyz.jiwook.toyBoard.vo.reponse.CommentVO;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepo commentRepo;
    private final PostRepo postRepo;
    private final SecurityUtil securityUtil;

    public CommentVO createComment(String content, long postId) {
        return CommentVO.fromEntity(
                commentRepo.save(CommentEntity.builder()
                        .content(content)
                        .post(postRepo.findById(postId).orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND_COMMENT)))
                        .author(securityUtil.getCurrentUser())
                        .build()));
    }

    public List<CommentVO> getCommentList(long postId) {
        return commentRepo.findAllByPostId(postId).stream()
                .map(CommentVO::fromEntity)
                .toList();
    }
}
