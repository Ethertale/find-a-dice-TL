package io.ethertale.findadicethymeleaf.group.model;

import io.ethertale.findadicethymeleaf.hero.model.Hero;
import io.ethertale.findadicethymeleaf.post.model.GroupPost;
import io.ethertale.findadicethymeleaf.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "game_groups") // avoid reserved keywords
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToMany(mappedBy = "groups")
    private Set<Hero> heroes;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GroupPost> groupPosts;

    @ManyToOne
    @JoinColumn(name = "created_by_id", nullable = false)
    private Hero createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

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
