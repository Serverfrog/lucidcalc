package de.serverfrog.lucidcalc.exception;

public class LibraryException extends RuntimeException {
    public LibraryException() {
    }

    public LibraryException(final String message) {
        super(message);
    }

    public LibraryException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public LibraryException(final Throwable cause) {
        super(cause);
    }

    public LibraryException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
