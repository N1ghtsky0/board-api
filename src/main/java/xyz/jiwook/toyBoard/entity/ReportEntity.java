package xyz.jiwook.toyBoard.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.jiwook.toyBoard.util.ReportType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "report")
public class ReportEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private ReportType reportType;

    private String reportReason;

    @Builder
    public ReportEntity(Long targetId, ReportType reportType, String reportReason) {
        this.targetId = targetId;
        this.reportType = reportType;
        this.reportReason = reportReason;
    }
}
