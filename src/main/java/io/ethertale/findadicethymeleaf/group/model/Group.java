package io.ethertale.findadicethymeleaf.group.model;

import io.ethertale.findadicethymeleaf.post.model.Post;
import io.ethertale.findadicethymeleaf.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "group")
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

    @OneToMany(mappedBy = "group")
    private Set<User> users;

    @OneToMany(mappedBy = "group")
    private Set<Post> posts;

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
