package xyz.jiwook.toyBoard.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.jiwook.toyBoard.entity.PostEntity;

public interface PostRepo extends JpaRepository<PostEntity, Long> {
    Page<PostEntity> findAllByDeletedIsFalseOrderByCreatedAtDesc(Pageable pageable);
}
