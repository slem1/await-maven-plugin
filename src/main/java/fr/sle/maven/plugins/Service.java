package fr.sle.maven.plugins;

/**
 *
 * Interface implemented by classes responsible for checking the availability of service
 *
 * @author slemoine
 */
public interface Service {

    /**
     * Checks if a service is available. Throws {@link ServiceUnavailableException} if the service
     * is unavailable. Otherwise, exit normally.
     *
     * @throws ServiceUnavailableException
     */
    void execute() throws ServiceUnavailableException;
}
