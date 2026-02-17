package io.ethertale.findadicethymeleaf.chat.model;

import io.ethertale.findadicethymeleaf.hero.model.Hero;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "chat_rooms")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String roomCode;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "chat_room_participants",
    joinColumns = @JoinColumn(name = "room_id"),
    inverseJoinColumns = @JoinColumn(name = "hero_id"))
    private Set<Hero> participants;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessages> messages = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
