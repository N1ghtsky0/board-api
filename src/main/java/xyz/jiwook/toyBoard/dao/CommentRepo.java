package xyz.jiwook.toyBoard.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.jiwook.toyBoard.entity.CommentEntity;

import java.util.List;

public interface CommentRepo extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByPostId(long postId);
}
