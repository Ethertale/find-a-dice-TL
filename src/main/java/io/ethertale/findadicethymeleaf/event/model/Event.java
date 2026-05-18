package io.ethertale.findadicethymeleaf.event.model;

import io.ethertale.findadicethymeleaf.hero.model.Hero;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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

    @ManyToMany
    @JoinTable(name = "event_interested_heroes", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "hero_id"))
    @Builder.Default
    private Set<Hero> interestedHeroes = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "event_going_heroes", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "hero_id"))
    @Builder.Default
    private Set<Hero> goingHeroes = new HashSet<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public List<Hero> getInterestedHeroesSortedAlphabeticallyAsc() {
        return interestedHeroes.stream().sorted(Comparator.comparing(Hero::getName)).toList();
    }
    public List<Hero> getGoingHeroesSortedAlphabeticallyAsc() {
        return goingHeroes.stream().sorted(Comparator.comparing(Hero::getName)).toList();
    }
}