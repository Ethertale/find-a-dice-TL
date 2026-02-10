package io.ethertale.findadicethymeleaf.group.model;

import io.ethertale.findadicethymeleaf.hero.model.Hero;
import io.ethertale.findadicethymeleaf.post.model.GroupPost;
import io.ethertale.findadicethymeleaf.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "game_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 40, nullable = false)
    private String name;

    @Column(length = 1000, nullable = false)
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
                + ":"
                + createdAt.getMinute();
    }

    public String slug(){
        return name.toLowerCase().replaceAll(" ", "-").replaceAll("[`~!@#$%^&*()_+\\\\;',./{}|:\"<>?]", "");
    }

    public String shortId(){
        return id.toString().substring(0, 8);
    }

    public List<GroupPost> getGroupPosts() {
        return groupPosts.stream().sorted(Comparator.comparing(GroupPost::getCreatedAt).reversed()).collect(Collectors.toList());
    }
}
