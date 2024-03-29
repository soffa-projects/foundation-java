package io.soffa.foundation.errors;

public class UnauthorizedException extends FunctionalException {

    private static final long serialVersionUID = 1L;

    public UnauthorizedException(String message, Object... args) {
        super(message, args);
    }

    public UnauthorizedException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

}
