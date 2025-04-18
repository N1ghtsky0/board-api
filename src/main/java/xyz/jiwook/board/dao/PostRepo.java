package xyz.jiwook.board.dao;

import org.springframework.data.repository.CrudRepository;
import xyz.jiwook.board.entity.PostEntity;

public interface PostRepo extends CrudRepository<PostEntity, Long>, PostCustomRepo {
}
