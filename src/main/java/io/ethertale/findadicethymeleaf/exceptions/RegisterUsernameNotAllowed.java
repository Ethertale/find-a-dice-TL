package io.ethertale.findadicethymeleaf.exceptions;

public class RegisterUsernameNotAllowed extends RuntimeException {
    public RegisterUsernameNotAllowed() {}
    public RegisterUsernameNotAllowed(String message) {
        super(message);
    }
}
