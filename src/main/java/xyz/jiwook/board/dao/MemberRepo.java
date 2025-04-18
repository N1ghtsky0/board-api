package xyz.jiwook.board.dao;

import org.springframework.data.repository.CrudRepository;
import xyz.jiwook.board.entity.MemberEntity;

import java.util.Optional;

public interface MemberRepo extends CrudRepository<MemberEntity, Long>, MemberCustomRepo {
    Optional<MemberEntity> findByUsername(String username);
}
