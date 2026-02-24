package io.ethertale.findadicethymeleaf.chat.service;

import io.ethertale.findadicethymeleaf.chat.model.ChatMessages;
import io.ethertale.findadicethymeleaf.chat.model.ChatRoom;
import io.ethertale.findadicethymeleaf.chat.repo.ChatMessagesRepo;
import io.ethertale.findadicethymeleaf.chat.repo.ChatRoomRepo;
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

    public ChatMessagesService(ChatMessagesRepo chatMessagesRepo, ChatRoomRepo chatRoomRepo) {
        this.chatMessagesRepo = chatMessagesRepo;
        this.chatRoomRepo = chatRoomRepo;
    }

    @Transactional
    public void sendMessage(String roomCode, User loggedUser, ChatRoomMessageDTO chatRoomMessageDTO) {
        ChatMessages newMessage = ChatMessages.builder()
                .chatRoom(chatRoomRepo.findByRoomCode(roomCode))
                .sender(loggedUser.getHero())
                .message(chatRoomMessageDTO.getMessage())
                .sentAt(LocalDateTime.now())
                .build();

        ChatRoom room = chatRoomRepo.findByRoomCode(roomCode);
        room.setUpdatedAt(LocalDateTime.now());

        chatMessagesRepo.save(newMessage);
        chatRoomRepo.save(room);

        log.info("{} {} - {}: {}", LocalDateTime.now(), loggedUser.getHero().getName(), roomCode, chatRoomMessageDTO.getMessage());
    }
}
