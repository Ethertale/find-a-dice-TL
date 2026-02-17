package io.ethertale.findadicethymeleaf.chat.service;

import io.ethertale.findadicethymeleaf.chat.model.ChatMessages;
import io.ethertale.findadicethymeleaf.chat.repo.ChatMessagesRepo;
import io.ethertale.findadicethymeleaf.chat.repo.ChatRoomRepo;
import io.ethertale.findadicethymeleaf.user.model.User;
import io.ethertale.findadicethymeleaf.web.dto.ChatRoomMessageDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatMessagesService {

    private final ChatMessagesRepo chatMessagesRepo;
    private final ChatRoomRepo chatRoomRepo;

    public ChatMessagesService(ChatMessagesRepo chatMessagesRepo, ChatRoomRepo chatRoomRepo) {
        this.chatMessagesRepo = chatMessagesRepo;
        this.chatRoomRepo = chatRoomRepo;
    }

    public void sendMessage(String roomCode, User loggedUser, ChatRoomMessageDTO chatRoomMessageDTO) {
        ChatMessages newMessage = ChatMessages.builder()
                .chatRoom(chatRoomRepo.findByRoomCode(roomCode))
                .sender(loggedUser.getHero())
                .message(chatRoomMessageDTO.getMessage())
                .sentAt(LocalDateTime.now())
                .build();

        chatMessagesRepo.save(newMessage);
    }
}
