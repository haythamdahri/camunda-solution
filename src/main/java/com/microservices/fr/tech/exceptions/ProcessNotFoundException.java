package com.microservices.fr.tech.exceptions;

/**
 * @author Haytham DAHRI
 * Process Not Found Exception to throw in case of functional errors
 */
public class ProcessNotFoundException extends RuntimeException {

    public ProcessNotFoundException() {
    }

    public ProcessNotFoundException(String message) {
        super(message);
    }

    public ProcessNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessNotFoundException(Throwable cause) {
        super(cause);
    }

    protected ProcessNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
