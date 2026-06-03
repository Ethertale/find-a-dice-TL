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
@Table(name = "character_sheets")
public class CharacterSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Sheet belongs to a membership, not directly to a hero. One hero can have different sheets across different campaigns.
    @OneToOne
    @JoinColumn(name = "membership_id", nullable = false)
    private CampaignMembership membership;

    // Identity
    @Column
    private String name;
    @Column
    private String age;
    @Column
    private String race;
    @Column
    private String gender;
    @Column
    private String charClass;
    @Column
    private String level;
    @Column
    private String background;
    @Column
    private String alignment;
    @Column
    private String imageUrl;

    // HP & Resource
    @Column
    private String healthPoints;
    @Column
    private String maxHealthPoints;
    @Column
    // Mana, Energy, Rage, etc.
    private String resourceName;
    @Column
    private String currResource;
    @Column
    private String maxResource;

    // Stats
    @Column
    private String strength;
    @Column
    private String dexterity;
    @Column
    private String constitution;
    @Column
    private String intelligence;
    @Column
    private String wisdom;
    @Column
    private String charisma;

    // Free form text areas
    @Column(columnDefinition = "TEXT")
    private String backstory;

    @Column(columnDefinition = "TEXT")
    private String inventory;

    @Column(columnDefinition = "TEXT")
    private String spells;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
