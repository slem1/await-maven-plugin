package fr.sle.maven.plugins;

/**
 *
 * General contract for MOJO parameter of connection config
 *
 * @author slemoine
 */
public interface MojoConnectionConfig {

    /**
     * Generates the underlying service according to the configuration
     *
     * @return the service
     */
    Service generateService();

    /**
     * Returns the connection priority
     *
     * @return the priority
     */
    int getPriority();
}
