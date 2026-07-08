package io.ethertale.findadicethymeleaf.feedbackReport.repo;

import io.ethertale.findadicethymeleaf.feedbackReport.model.FeedbackReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FeedbackReportRepo extends JpaRepository<FeedbackReport, UUID> {
}
