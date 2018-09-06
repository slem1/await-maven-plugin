package fr.sle.maven.plugins;

/**
 *
 * Throw it if a service is unavailable
 *
 * @author slemoine
 */
public class ServiceUnavailableException extends Exception {

    public ServiceUnavailableException(String message){
        super(message);
    }

    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

}
