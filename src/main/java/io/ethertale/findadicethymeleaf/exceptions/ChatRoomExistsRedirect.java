package io.ethertale.findadicethymeleaf.exceptions;

public class ChatRoomExistsRedirect extends RuntimeException {
    public ChatRoomExistsRedirect() {
    }

    public ChatRoomExistsRedirect(String message) {
        super(message);
    }
}
