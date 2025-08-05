package io.ethertale.findadicethymeleaf.exceptions;

public class LoginProfileWrongCredentials extends RuntimeException {
    public LoginProfileWrongCredentials() {}
    public LoginProfileWrongCredentials(String message) {
        super(message);
    }
}
