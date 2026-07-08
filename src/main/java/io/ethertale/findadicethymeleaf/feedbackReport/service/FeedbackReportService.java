package io.ethertale.findadicethymeleaf.feedbackReport.service;

import io.ethertale.findadicethymeleaf.feedbackReport.model.FeedbackReport;
import io.ethertale.findadicethymeleaf.feedbackReport.repo.FeedbackReportRepo;
import io.ethertale.findadicethymeleaf.web.dto.FeedbackReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedbackReportService {

    private final FeedbackReportRepo feedbackReportRepo;

    @Autowired
    public FeedbackReportService(FeedbackReportRepo feedbackReportRepo) {
        this.feedbackReportRepo = feedbackReportRepo;
    }

    @Transactional
    public void submitReport(FeedbackReportDTO reportDTO){

        // TODO add exceptions checking

        FeedbackReport report = FeedbackReport.builder()
                .firstName(reportDTO.getFirstName())
                .lastName(reportDTO.getLastName())
                .email(reportDTO.getEmail())
                .content(reportDTO.getContent())
                .type(reportDTO.getType())
                .createdAt(LocalDateTime.now())
                .build();

        feedbackReportRepo.save(report);
    }

    public List<FeedbackReport> getAllFeedbackReports(){
        return feedbackReportRepo.findAll();
    }
}
