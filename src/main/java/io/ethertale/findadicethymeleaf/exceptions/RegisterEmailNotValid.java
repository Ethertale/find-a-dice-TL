package io.ethertale.findadicethymeleaf.exceptions;

public class RegisterEmailNotValid extends RuntimeException {
    public RegisterEmailNotValid() {}
    public RegisterEmailNotValid(String message) {
        super(message);
    }
}
