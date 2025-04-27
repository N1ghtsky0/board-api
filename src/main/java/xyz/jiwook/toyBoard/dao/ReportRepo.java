package xyz.jiwook.toyBoard.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.jiwook.toyBoard.entity.ReportEntity;

public interface ReportRepo extends JpaRepository<ReportEntity, Long> {
}
