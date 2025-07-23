package io.ethertale.findadicethymeleaf.hero.model;

import io.ethertale.findadicethymeleaf.user.model.Genders;
import io.ethertale.findadicethymeleaf.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "hero")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hero {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(mappedBy = "hero")
    private User user;

    @Column(nullable = false)
    private String name;

    @Column
    private int age;

    @Column
    @Enumerated(EnumType.STRING)
    private Genders gender;

    @Column(nullable = false, name = "char_class")
    @Enumerated(EnumType.STRING)
    private Classes charClass;

    @Column(length = 10000)
    private String description;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    private Alignment alignment;

    @Column(nullable = false)
    private Backgrounds background;

    @Column(nullable = false)
    private int strength;

    @Column(nullable = false)
    private int dexterity;

    @Column(nullable = false)
    private int constitution;

    @Column(nullable = false)
    private int intelligence;

    @Column(nullable = false)
    private int wisdom;

    @Column(nullable = false)
    private int charisma;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

}
