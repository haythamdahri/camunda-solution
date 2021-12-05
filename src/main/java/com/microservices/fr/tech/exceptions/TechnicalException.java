package com.microservices.fr.tech.exceptions;

/**
 * @author Haytham DAHRI
 * Technical Exception to throw in case of technical errors
 */
public class TechnicalException extends RuntimeException {

    public TechnicalException() {
    }

    public TechnicalException(String message) {
        super(message);
    }

    public TechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public TechnicalException(Throwable cause) {
        super(cause);
    }

    protected TechnicalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
