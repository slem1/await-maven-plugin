package com.github.slem1.await;

/**
 * Plugin configuration for TCP connection
 *
 * @author slemoine
 */
public class TCPConnectionConfig implements MojoConnectionConfig {

    private String host;

    private Integer port;

    private int priority = Integer.MAX_VALUE;

    /**
     * Default constructor used by maven.
     */
    public TCPConnectionConfig() {

    }

    /**
     * Convenient constructor to create instance.
     *
     * @param host     tcp host.
     * @param port     tcp port.
     * @param priority the connection priority. 0 is the highest priority.
     */
    public TCPConnectionConfig(String host, Integer port, int priority) {
        this.host = host;
        this.port = port;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public Service buildService() {
        return new TCPService(host, port);
    }
}
