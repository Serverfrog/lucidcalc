package de.serverfrog.lucidcalc.exception;

public class ObjectCreationException extends RuntimeException {
    public ObjectCreationException() {
    }

    public ObjectCreationException(final String message) {
        super(message);
    }

    public ObjectCreationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ObjectCreationException(final Throwable cause) {
        super(cause);
    }

    public ObjectCreationException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
