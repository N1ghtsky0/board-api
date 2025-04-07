package xyz.jiwook.board.domain.member.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.jiwook.board.domain.member.model.MemberEntity;

import java.util.Optional;

public interface MemberCRUDRepo extends CrudRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByUsername(String username);
}
