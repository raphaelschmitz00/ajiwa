package net.roughdesign.ajiwa.exceptions;

public class NoAvailableConstructorException extends RuntimeException {

    public NoAvailableConstructorException(String errorMessage) {
        super(errorMessage);
    }
}
