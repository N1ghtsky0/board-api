package xyz.jiwook.toyBoard.vo.reponse;

import lombok.Getter;
import xyz.jiwook.toyBoard.entity.PostEntity;
import xyz.jiwook.toyBoard.util.DateTimeUtil;

@Getter
public class PostSummaryVO {
    private long id;
    private String title;
    private String author;
    private long viewCount;
    private String createdDate;

    private PostSummaryVO() {}

    public static PostSummaryVO fromEntity(PostEntity postEntity) {
        PostSummaryVO postSummaryVO = new PostSummaryVO();
        postSummaryVO.id = postEntity.getId();
        postSummaryVO.title = postEntity.getTitle();
        postSummaryVO.author = postEntity.getCreatedBy();
        postSummaryVO.viewCount = postEntity.getViewCount();
        postSummaryVO.createdDate = DateTimeUtil.summary(postEntity.getCreatedAt());
        return postSummaryVO;
    }
}
