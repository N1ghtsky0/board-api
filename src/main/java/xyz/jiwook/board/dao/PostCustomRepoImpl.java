package xyz.jiwook.board.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostCustomRepoImpl implements PostCustomRepo {
    private final JPAQueryFactory jpaQueryFactory;
}
