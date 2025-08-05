package io.ethertale.findadicethymeleaf.exceptions;

public class RegisterPasswordNotInCharRange extends RuntimeException {
    public RegisterPasswordNotInCharRange() {}
    public RegisterPasswordNotInCharRange(String message) {
        super(message);
    }
}
