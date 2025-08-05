package io.ethertale.findadicethymeleaf.exceptions;

public class RegisterUsernameNotInCharRange extends RuntimeException {
    public RegisterUsernameNotInCharRange() {}
    public RegisterUsernameNotInCharRange(String message) {
        super(message);
    }
}
