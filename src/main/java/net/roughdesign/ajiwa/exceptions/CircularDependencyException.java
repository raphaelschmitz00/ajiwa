package net.roughdesign.ajiwa.exceptions;

public class CircularDependencyException extends RuntimeException {

    public CircularDependencyException(String errorMessage) {
        super(errorMessage);
    }
}
