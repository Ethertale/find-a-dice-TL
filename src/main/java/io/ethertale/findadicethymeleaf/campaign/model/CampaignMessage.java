package io.ethertale.findadicethymeleaf.campaign.model;

import io.ethertale.findadicethymeleaf.hero.model.Hero;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "campaign_messages")
public class CampaignMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Hero sender;

    @Column(nullable = false, length = 1500)
    private String message;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    public String getFormattedTime() {
        return String.format("%02d:%02d | %d %s %d", sentAt.getHour(), sentAt.getMinute(), sentAt.getDayOfMonth(), sentAt.getMonth(), sentAt.getYear());
    }
}
