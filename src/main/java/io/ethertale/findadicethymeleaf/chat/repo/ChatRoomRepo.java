package io.ethertale.findadicethymeleaf.chat.repo;

import io.ethertale.findadicethymeleaf.chat.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatRoomRepo extends JpaRepository<ChatRoom, UUID> {
    ChatRoom findByRoomCode(String roomCode);
}
