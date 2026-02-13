package io.ethertale.findadicethymeleaf.event.model;

import io.ethertale.findadicethymeleaf.hero.model.Hero;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String location;

    @ManyToOne
    @JoinColumn(name = "created_by_id", nullable = false)
    private Hero createdByEvent;

    @Column(nullable = false)
    private LocalDate startTime;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private int interested;

    @Column(nullable = false)
    private int going;

    @ManyToMany(mappedBy = "events")
    private Set<Hero> heroes;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
