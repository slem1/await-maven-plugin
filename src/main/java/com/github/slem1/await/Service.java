package com.github.slem1.await;

/**
 * Interface implemented by classes responsible for checking the availability of service.
 *
 * @author slemoine
 */
public interface Service {

    /**
     * Try to connect to the service. Throws {@link ServiceUnavailableException} if the service
     * is unavailable. Otherwise, exit normally.
     *
     * @throws ServiceUnavailableException if the service cannot be reached or is not ready.
     */
    void execute() throws ServiceUnavailableException;
}
