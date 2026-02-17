package io.ethertale.findadicethymeleaf.chat.service;

import io.ethertale.findadicethymeleaf.chat.model.ChatRoom;
import io.ethertale.findadicethymeleaf.chat.repo.ChatRoomRepo;
import io.ethertale.findadicethymeleaf.exceptions.ChatHeroNotInChatRoom;
import io.ethertale.findadicethymeleaf.exceptions.ChatRoomDoesNotExist;
import io.ethertale.findadicethymeleaf.exceptions.ChatRoomExistsRedirect;
import io.ethertale.findadicethymeleaf.hero.model.Hero;
import io.ethertale.findadicethymeleaf.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ChatRoomService {

    private final ChatRoomRepo chatRoomRepo;

    @Autowired
    public ChatRoomService(ChatRoomRepo chatRoomRepo) {
        this.chatRoomRepo = chatRoomRepo;
    }

    /**
     * Checks if the current character has access to a specific chat room.
     * This is the core security check for private chats.
     *
     * @param roomCode The unique identifier for the chat room
     * @param currentCharacter The character trying to access the room
     * @return true if the character is a participant, false otherwise
     */

    public boolean canAccessChatRoom(String roomCode, Hero currentCharacter) {
        ChatRoom room = chatRoomRepo.findByRoomCode(roomCode);

        if (room == null) {
            throw new ChatRoomDoesNotExist(roomCode);
        }

        return room.getParticipants().contains(currentCharacter);
    }

    /**
     * Gets a chat room if the user has access, otherwise returns null.
     * This combines retrieval with authorization.
     */
    public ChatRoom getChatRoomIfAuthorized(String roomCode, Hero currentCharacter) {
        ChatRoom room = chatRoomRepo.findByRoomCode(roomCode);
        if (room == null) {
            throw new ChatRoomDoesNotExist(roomCode);
        }
        // Security check: is this character a participant?
        if (!room.getParticipants().contains(currentCharacter)) {
            throw new ChatHeroNotInChatRoom();
        }

        return room;
    }

    public List<ChatRoom> getAllChatRoomsSortedByLastActivity(User loggedUser){
        return loggedUser.getHero().getChatRooms().stream().sorted(Comparator.comparing(ChatRoom::getUpdatedAt).reversed()).toList();
    }

    public void createChatRoom(Hero hero1, Hero hero2) {

        if (chatRoomRepo.existsChatRoomBetween(hero1, hero2)){
            throw new ChatRoomExistsRedirect();
        }

        String roomCode;
        List<Hero> chatRoomParticipants = new ArrayList<>();
        chatRoomParticipants.add(hero1);
        chatRoomParticipants.add(hero2);

        do {
            roomCode = generateRandomCode();
        } while (chatRoomRepo.existsByRoomCode(roomCode));

        ChatRoom room = ChatRoom.builder()
                .roomCode(roomCode)
                .participants(chatRoomParticipants)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        chatRoomRepo.save(room);
    }

    private String generateRandomCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 15; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }

        return code.toString();
    }
}

