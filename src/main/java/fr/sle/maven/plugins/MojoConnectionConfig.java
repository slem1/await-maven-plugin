package fr.sle.maven.plugins;

/**
 *
 * General contract for MOJO parameter of connection config
 *
 * @author slemoine
 */
public interface MojoConnectionConfig {

    /**
     * Build the underlying service implementation according to this configuration
     *
     * @return the service
     */
    Service buildService();

    /**
     * Returns the connection priority
     *
     * @return the priority
     */
    int getPriority();
}
