package io.ethertale.findadicethymeleaf.exceptions;

public class GroupDoesNotExistException extends RuntimeException {
    public GroupDoesNotExistException() {}
    public GroupDoesNotExistException(String message) {
        super(message);
    }
}
