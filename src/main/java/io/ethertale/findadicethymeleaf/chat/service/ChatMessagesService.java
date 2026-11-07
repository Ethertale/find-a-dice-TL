package io.ethertale.findadicethymeleaf.chat.service;

import io.ethertale.findadicethymeleaf.chat.model.ChatMessages;
import io.ethertale.findadicethymeleaf.chat.model.ChatRoom;
import io.ethertale.findadicethymeleaf.chat.repo.ChatMessagesRepo;
import io.ethertale.findadicethymeleaf.chat.repo.ChatRoomRepo;
import io.ethertale.findadicethymeleaf.moderation.model.ModerationSource;
import io.ethertale.findadicethymeleaf.moderation.service.ModerationService;
import io.ethertale.findadicethymeleaf.user.model.User;
import io.ethertale.findadicethymeleaf.web.dto.ChatRoomMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ChatMessagesService {

    private final ChatMessagesRepo chatMessagesRepo;
    private final ChatRoomRepo chatRoomRepo;
    private final ModerationService moderationService;

    public ChatMessagesService(ChatMessagesRepo chatMessagesRepo, ChatRoomRepo chatRoomRepo, ModerationService moderationService) {
        this.chatMessagesRepo = chatMessagesRepo;
        this.chatRoomRepo = chatRoomRepo;
        this.moderationService = moderationService;
    }

    @Transactional
    public ChatMessages sendMessage(String roomCode, User loggedUser, ChatRoomMessageDTO chatRoomMessageDTO) {
        ChatMessages newMessage = ChatMessages.builder()
                .chatRoom(chatRoomRepo.findByRoomCode(roomCode))
                .sender(loggedUser.getHero())
                .message(moderationService.moderate(chatRoomMessageDTO.getMessage(), ModerationSource.CHAT_MESSAGE, loggedUser.getId()))
                .sentAt(LocalDateTime.now())
                .build();

        ChatRoom room = chatRoomRepo.findByRoomCode(roomCode);
        room.setUpdatedAt(LocalDateTime.now());

        chatMessagesRepo.save(newMessage);
        chatRoomRepo.save(room);

        log.info("{} {} - {}: {}", LocalDateTime.now(), loggedUser.getHero().getName(), roomCode, newMessage.getMessage());
        return newMessage;
    }
}
