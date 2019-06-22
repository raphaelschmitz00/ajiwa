package net.roughdesign.ajiwa.exceptions;

public class AmbiguousBindingException extends RuntimeException {

    public AmbiguousBindingException(String errorMessage) {
        super(errorMessage);
    }
}
