package xyz.jiwook.toyBoard.dao;

import org.springframework.data.repository.CrudRepository;
import xyz.jiwook.toyBoard.entity.RefreshTokenEntity;

public interface RefreshTokenRepo extends CrudRepository<RefreshTokenEntity, String> {
}
