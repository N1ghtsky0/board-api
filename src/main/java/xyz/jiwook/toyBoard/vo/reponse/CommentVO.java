package xyz.jiwook.toyBoard.vo.reponse;

import lombok.Getter;
import xyz.jiwook.toyBoard.entity.CommentEntity;
import xyz.jiwook.toyBoard.util.DateTimeUtil;

@Getter
public class CommentVO {
    private long id;
    private String content;
    private String author;
    private String createdDate;

    private CommentVO() {}

    public static CommentVO fromEntity(CommentEntity commentEntity) {
        CommentVO commentVO = new CommentVO();
        commentVO.id = commentEntity.getId();
        commentVO.content = commentEntity.isDeleted() ? commentEntity.getDeletedReason() : commentEntity.getContent();
        commentVO.author = commentEntity.getCreatedBy();
        commentVO.createdDate = DateTimeUtil.full(commentEntity.getCreatedAt());
        return commentVO;
    }
}
