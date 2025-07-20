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

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

}
