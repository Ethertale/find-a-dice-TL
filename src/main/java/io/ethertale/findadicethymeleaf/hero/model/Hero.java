package io.ethertale.findadicethymeleaf.hero.model;

import io.ethertale.findadicethymeleaf.chat.model.ChatMessages;
import io.ethertale.findadicethymeleaf.chat.model.ChatRoom;
import io.ethertale.findadicethymeleaf.event.model.Event;
import io.ethertale.findadicethymeleaf.group.model.Group;
import io.ethertale.findadicethymeleaf.post.model.Comment;
import io.ethertale.findadicethymeleaf.post.model.GroupPost;
import io.ethertale.findadicethymeleaf.user.model.Genders;
import io.ethertale.findadicethymeleaf.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "heroes")
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
    private Races race;

    @Column
    @Enumerated(EnumType.STRING)
    private Genders gender;

    @Column(nullable = false, name = "char_class")
    @Enumerated(EnumType.STRING)
    private Classes charClass;

    @Column
    private int level;

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

    @OneToMany(mappedBy = "hero", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GroupPost> groupPosts;

    @OneToMany(mappedBy = "hero", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    @ManyToMany
    @JoinTable(
            name = "hero_group_link", // join table
            joinColumns = @JoinColumn(name = "hero_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<Group> groups;

    @ManyToMany
    @JoinTable(
            name = "hero_event_link", // join table
            joinColumns = @JoinColumn(name = "hero_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private Set<Event> events;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Group> createdGroups;

    @OneToMany(mappedBy = "createdByEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Event> createdEvents;

    @ManyToMany(mappedBy = "participants", fetch = FetchType.LAZY)
    private Set<ChatRoom> chatRooms = new HashSet<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatMessages> sentMessages = new HashSet<>();

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

}
