package xyz.jiwook.board.domain.board.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.jiwook.board.domain.board.model.BoardEntity;

public interface BoardCRUDRepo extends CrudRepository<BoardEntity, Long> {
}
