package xyz.jiwook.toyBoard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.jiwook.toyBoard.dao.ReportRepo;
import xyz.jiwook.toyBoard.entity.ReportEntity;
import xyz.jiwook.toyBoard.util.ReportType;
import xyz.jiwook.toyBoard.vo.request.ReportVO;

@RequiredArgsConstructor
@Service
public class ReportService {
    private final ReportRepo reportRepo;

    public void reportPost(ReportVO reportVO) {
        createReport(reportVO, ReportType.POST);
    }

    public void reportComment(ReportVO reportVO) {
        createReport(reportVO, ReportType.COMMENT);
    }

    public void reportUser(ReportVO reportVO) {
        createReport(reportVO, ReportType.USER);
    }

    private void createReport(ReportVO reportVO, ReportType reportType) {
        reportRepo.save(ReportEntity.builder()
                .targetId(reportVO.getTargetId())
                .reportReason(reportVO.getReason())
                .reportType(reportType)
                .build());
    }
}
