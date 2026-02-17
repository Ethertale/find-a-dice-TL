package io.ethertale.findadicethymeleaf.exceptions;

public class ChatRoomDoesNotExist extends RuntimeException {
    public ChatRoomDoesNotExist() {
    }

    public ChatRoomDoesNotExist(String message) {
        super(message);
    }
}
