package com.github.slem1.await;

/**
 * General contract for MOJO parameter of connection config.
 *
 * @author slemoine
 */
public interface MojoConnectionConfig {

    /**
     * Build the underlying service implementation according to this configuration.
     *
     * @return the service.
     */
    Service buildService();

    /**
     * Returns the element priority.
     * <p>
     * The lower the value is the higher the priority is.
     *
     * @return the priority.
     */
    int getPriority();
}
