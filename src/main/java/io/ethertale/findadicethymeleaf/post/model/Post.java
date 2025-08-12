package io.ethertale.findadicethymeleaf.post.model;

import io.ethertale.findadicethymeleaf.group.model.Group;
import io.ethertale.findadicethymeleaf.hero.model.Hero;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "hero_id", nullable = false)
    private Hero hero;

    @OneToMany(mappedBy = "post")
    private Set<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(nullable = false)
    private String description;

    @Column
    private String image;

    @Column(nullable = false)
    private int likes;

    public String getTimestamp() {
        return createdAt.getDayOfMonth()
                + " "
                + createdAt.getMonth().toString()
                + " "
                + createdAt.getYear()
                + ", at "
                + createdAt.getHour()
                + " "
                + createdAt.getMinute();
    }
}
