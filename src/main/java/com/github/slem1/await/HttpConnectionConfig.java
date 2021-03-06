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

    private int priority = Integer.MAX_VALUE;

    private boolean skipSSLCertVerification;

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
     * @param skipSSLCertVerification   https connections ignore certs.
     */

    public HttpConnectionConfig(URL url, int statusCode, int priority, boolean skipSSLCertVerification) {
        this.url = url;
        this.statusCode = statusCode;
        this.priority = priority;
        this.skipSSLCertVerification = skipSSLCertVerification;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public Service buildService() {
        return new HttpService(url, statusCode, skipSSLCertVerification);
    }

}
