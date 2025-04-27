package xyz.jiwook.toyBoard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.jiwook.toyBoard.service.ReportService;
import xyz.jiwook.toyBoard.vo.request.ReportVO;

@RequiredArgsConstructor
@RestController("/report")
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/posts")
    public ResponseEntity<Void> reportPost(@RequestBody ReportVO reportVO) {
        reportService.reportPost(reportVO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comments")
    public ResponseEntity<Void> reportComment(@RequestBody ReportVO reportVO) {
        reportService.reportComment(reportVO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users")
    public ResponseEntity<Void> reportUser(@RequestBody ReportVO reportVO) {
        reportService.reportUser(reportVO);
        return ResponseEntity.ok().build();
    }
}
