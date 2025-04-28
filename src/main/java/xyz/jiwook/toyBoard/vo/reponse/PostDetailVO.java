package xyz.jiwook.toyBoard.vo.reponse;

import lombok.Getter;
import xyz.jiwook.toyBoard.entity.PostEntity;
import xyz.jiwook.toyBoard.util.DateTimeUtil;

@Getter
public class PostDetailVO {
    private long id;
    private String title;
    private String content;
    private long viewCount;
    private String author;
    private String createdDate;

    private PostDetailVO() {}

    public static PostDetailVO fromEntity(PostEntity postEntity) {
        PostDetailVO postDetailVO = new PostDetailVO();
        if (postEntity != null) {
            postDetailVO.id = postEntity.getId();
            postDetailVO.title = postEntity.getTitle();
            postDetailVO.content = postEntity.getContent();
            postDetailVO.viewCount = postEntity.getViewCount();
            postDetailVO.author = postEntity.getCreatedBy();
            postDetailVO.createdDate = DateTimeUtil.full(postEntity.getCreatedAt());
        }
        return postDetailVO;
    }
}
