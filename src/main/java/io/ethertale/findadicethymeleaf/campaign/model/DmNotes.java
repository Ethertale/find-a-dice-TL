package io.ethertale.findadicethymeleaf.campaign.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "dm_notes")
public class DmNotes {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    // Free form text where the DM writes whatever they need (NPCs, enemies, story notes, etc.)
    @Column(columnDefinition = "LONGTEXT")
    private String content;
}

