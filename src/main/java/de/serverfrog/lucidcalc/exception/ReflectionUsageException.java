package de.serverfrog.lucidcalc.exception;

public class ReflectionUsageException extends RuntimeException {
    public ReflectionUsageException() {
    }

    public ReflectionUsageException(final String message) {
        super(message);
    }

    public ReflectionUsageException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ReflectionUsageException(final Throwable cause) {
        super(cause);
    }

    public ReflectionUsageException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
