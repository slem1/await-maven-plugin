package com.github.slem1.await;

/**
 * Plugin configuration for TCP connection
 *
 * @author slemoine
 */
public class TCPConnectionConfig implements MojoConnectionConfig {

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
