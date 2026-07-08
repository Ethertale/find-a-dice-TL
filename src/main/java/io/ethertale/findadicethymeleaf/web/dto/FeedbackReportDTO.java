package io.ethertale.findadicethymeleaf.web.dto;

import io.ethertale.findadicethymeleaf.feedbackReport.model.FeedbackReportType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackReportDTO {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String content;
    private FeedbackReportType type;
}
