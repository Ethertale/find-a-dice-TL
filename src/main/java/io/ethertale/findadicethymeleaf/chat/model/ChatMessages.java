package io.ethertale.findadicethymeleaf.chat.model;

import io.ethertale.findadicethymeleaf.hero.model.Hero;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "chat_messages")
public class ChatMessages {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Hero sender;

    @Column(nullable = false, length = 1500)
    private String message;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    public String getTimeForSentMessage() {
        return sentAt.getHour() +
                ":" +
                sentAt.getMinute() +
                " | " +
                sentAt.getDayOfMonth() +
                " " +
                sentAt.getMonth() +
                " " +
                sentAt.getYear();
    }
}
