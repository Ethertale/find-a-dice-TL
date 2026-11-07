package io.ethertale.findadicethymeleaf.moderation.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "moderation_flags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModerationFlag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // VARCHAR instead of Hibernate's default MySQL ENUM, so adding new values later doesn't need an ALTER TABLE
    @Column(nullable = false, columnDefinition = "VARCHAR(32)")
    @Enumerated(EnumType.STRING)
    private ModerationSource source;

    @Column(nullable = false, columnDefinition = "VARCHAR(16)")
    @Enumerated(EnumType.STRING)
    private ModerationStatus status;

    @Column
    private UUID authorUserId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String originalText;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String maskedText;

    @Column(nullable = false)
    private LocalDateTime flaggedAt;

    public String getFormatedTime(){
        return String.format("%d/%d/%d %02d:%02d",
                flaggedAt.getDayOfMonth(), flaggedAt.getMonthValue(), flaggedAt.getYear(),
                flaggedAt.getHour(), flaggedAt.getMinute());
    }
}
