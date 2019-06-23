package net.roughdesign.ajiwa.exceptions;


public class UnresolvedParameterException extends RuntimeException {


    public UnresolvedParameterException(final Class<?> klass) {

        super("Can't auto-bind " + klass);
    }
}


