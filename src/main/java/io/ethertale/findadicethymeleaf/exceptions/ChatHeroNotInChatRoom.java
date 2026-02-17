package io.ethertale.findadicethymeleaf.exceptions;

public class ChatHeroNotInChatRoom extends RuntimeException {
    public ChatHeroNotInChatRoom() {
    }

    public ChatHeroNotInChatRoom(String message) {
        super(message);
    }
}
