package xyz.jiwook.toyBoard.vo.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReportVO {
    private long targetId;
    private String reason = "";
}
