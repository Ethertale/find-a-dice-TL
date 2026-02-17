package io.ethertale.findadicethymeleaf.chat.service;

import io.ethertale.findadicethymeleaf.chat.model.ChatRoom;
import io.ethertale.findadicethymeleaf.chat.repo.ChatRoomRepo;
import io.ethertale.findadicethymeleaf.exceptions.ChatHeroNotInChatRoom;
import io.ethertale.findadicethymeleaf.exceptions.ChatRoomDoesNotExist;
import io.ethertale.findadicethymeleaf.hero.model.Hero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

