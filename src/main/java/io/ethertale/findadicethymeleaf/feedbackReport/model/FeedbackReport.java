package io.ethertale.findadicethymeleaf.feedbackReport.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "feedback_report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackReport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 10000)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FeedbackReportType type;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public String getCreatedAtFormated(){
        return createdAt.getDayOfMonth() + " " + createdAt.getMonth() + " " + createdAt.getYear() + " - " + createdAt.getHour() + ":" + createdAt.getMinute();
    }
}
