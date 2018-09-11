package com.github.slem1.await;

/**
 * Throw it if a service is unavailable. 
 *
 * @author slemoine
 */
public class ServiceUnavailableException extends Exception {

    /**
     * creates new instance with {@code message}.
     *
     * @param message the exception message.
     */
    public ServiceUnavailableException(String message) {
        super(message);
    }

    /**
     * create new instance {@code message} and the cause {@code cause}.
     *
     * @param message the exception message.
     * @param cause   the underlying cause.
     */
    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

}
