package io.ethertale.findadicethymeleaf.chat.repo;

import io.ethertale.findadicethymeleaf.chat.model.ChatMessages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatMessagesRepo extends JpaRepository<ChatMessages, UUID> {
}
