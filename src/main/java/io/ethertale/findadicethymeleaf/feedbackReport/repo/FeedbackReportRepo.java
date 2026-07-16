package io.ethertale.findadicethymeleaf.feedbackReport.repo;

import io.ethertale.findadicethymeleaf.feedbackReport.model.FeedbackReport;
import io.ethertale.findadicethymeleaf.feedbackReport.model.FeedbackReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FeedbackReportRepo extends JpaRepository<FeedbackReport, UUID> {
    List<FeedbackReport> findAllByType(FeedbackReportType type);
}
