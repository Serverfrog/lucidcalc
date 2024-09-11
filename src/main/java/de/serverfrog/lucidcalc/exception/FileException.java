package de.serverfrog.lucidcalc.exception;

import java.io.IOException;

public class FileException extends RuntimeException {
    public FileException() {
    }

    public FileException(final String message) {
        super(message);
    }

    public FileException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public FileException(final Throwable cause) {
        super(cause);
    }

    public FileException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
