package net.roughdesign.ajiwa.exceptions;

public class NoAvailableConstructorException extends RuntimeException {

    public NoAvailableConstructorException(final String errorMessage) {
        super(errorMessage);
    }
}
