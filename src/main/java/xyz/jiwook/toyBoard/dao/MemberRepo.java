package xyz.jiwook.toyBoard.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.jiwook.toyBoard.entity.MemberEntity;

import java.util.Optional;

public interface MemberRepo extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByUsername(String username);
}
