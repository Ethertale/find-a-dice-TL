package io.ethertale.findadicethymeleaf.chat.repo;

import io.ethertale.findadicethymeleaf.chat.model.ChatRoom;
import io.ethertale.findadicethymeleaf.hero.model.Hero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatRoomRepo extends JpaRepository<ChatRoom, UUID> {
    ChatRoom findByRoomCode(String roomCode);

    boolean existsByRoomCode(String roomCode);

    @Query("""
    SELECT COUNT(c) > 0 FROM ChatRoom c
    WHERE :hero1 MEMBER OF c.participants
    AND :hero2 MEMBER OF c.participants
    AND SIZE(c.participants) = 2
    """)
    boolean existsChatRoomBetween(@Param("hero1") Hero hero1, @Param("hero2") Hero hero2);
}
