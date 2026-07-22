package io.ethertale.findadicethymeleaf.exceptions;

public class BannedException extends RuntimeException {
    public BannedException() {
    }

    public BannedException(String message) {
        super(message);
    }
}
