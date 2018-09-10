package fr.sle.maven.plugins;

/**
 * Plugin configuration for TCP connection
 *
 * @author slemoine
 */
public class TCPConfig implements MojoConnectionConfig {

    private String host;

    private Integer port;

    private int priority;

    public int getPriority() {
        return priority;
    }

    @Override
    public Service buildService(){
        return new TCPService(host, port);
    }
}
