package io.ethertale.findadicethymeleaf.user.model;

import io.ethertale.findadicethymeleaf.hero.model.Hero;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoles role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hero_id", referencedColumnName = "id")
    private Hero hero;

    @Column
    private String imageUrl;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;
}
