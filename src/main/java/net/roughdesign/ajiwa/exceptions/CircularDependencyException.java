package net.roughdesign.ajiwa.exceptions;

public class CircularDependencyException extends RuntimeException {

    public CircularDependencyException(final String errorMessage) {
        super(errorMessage);
    }
}


