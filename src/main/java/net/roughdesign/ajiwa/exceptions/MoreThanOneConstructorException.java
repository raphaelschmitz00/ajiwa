package net.roughdesign.ajiwa.exceptions;

public class MoreThanOneConstructorException extends RuntimeException {

    public MoreThanOneConstructorException(final String errorMessage) {
        super(errorMessage);
    }
}


