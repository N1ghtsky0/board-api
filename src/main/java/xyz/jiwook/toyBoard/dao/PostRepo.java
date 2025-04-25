package xyz.jiwook.toyBoard.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.jiwook.toyBoard.entity.PostEntity;

import java.util.Optional;

public interface PostRepo extends JpaRepository<PostEntity, Long> {
    Page<PostEntity> findAllByDeletedIsFalseOrderByCreatedAtDesc(Pageable pageable);

    Optional<PostEntity> findByIdAndDeletedIsFalse(long id);

    Page<PostEntity> findAllByDeletedIsFalseAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(Pageable pageable, String title);

    Page<PostEntity> findAllByDeletedIsFalseAndContentContainingIgnoreCaseOrderByCreatedAtDesc(Pageable pageable, String content);

    Page<PostEntity> findAllByDeletedIsFalseAndCreatedByContainingIgnoreCaseOrderByCreatedAtDesc(Pageable pageable, String author);
}
