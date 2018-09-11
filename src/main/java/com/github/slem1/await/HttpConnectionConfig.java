package com.github.slem1.await;

import java.net.URL;

/**
 * Plugin configuration POJO for http service.
 *
 * @author slemoine
 */
public class HttpConnectionConfig implements MojoConnectionConfig {

    private URL url;

    private int statusCode;

    private int priority;

    /**
     * Default constructor used by maven.
     */
    public HttpConnectionConfig() {

    }

    /**
     * Convenient constructor to create instance.
     *
     * @param url        url of the service.
     * @param statusCode expected response status code.
     * @param priority   the connection priority.
     */
    public HttpConnectionConfig(URL url, int statusCode, int priority) {
        this.url = url;
        this.statusCode = statusCode;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public Service buildService() {
        return new HttpService(url, statusCode);
    }

}
