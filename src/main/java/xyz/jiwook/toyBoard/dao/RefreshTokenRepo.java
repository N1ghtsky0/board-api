package xyz.jiwook.toyBoard.dao;

import org.springframework.data.repository.CrudRepository;
import xyz.jiwook.toyBoard.entity.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenRepo extends CrudRepository<RefreshTokenEntity, String> {
    Optional<RefreshTokenEntity> findByTokenAndUsername(String token, String username);
}
