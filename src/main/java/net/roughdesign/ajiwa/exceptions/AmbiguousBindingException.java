package net.roughdesign.ajiwa.exceptions;

public class AmbiguousBindingException extends RuntimeException {

    public AmbiguousBindingException(final String errorMessage) {
        super(errorMessage);
    }
}
