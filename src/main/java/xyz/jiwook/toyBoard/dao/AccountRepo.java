package xyz.jiwook.toyBoard.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.jiwook.toyBoard.entity.BaseAccountEntity;

import java.util.Optional;

public interface AccountRepo extends JpaRepository<BaseAccountEntity, Long> {
    Optional<BaseAccountEntity> findByUsername(String username);
}
