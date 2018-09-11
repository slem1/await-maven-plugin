package com.github.slem1.await;

import java.net.URL;

/**
 * Plugin configuration POJO for http service.
 *
 * @author slemoine
 */
public class HttpConfig implements MojoConnectionConfig {

    private URL url;

    private int statusCode;

    private int priority;

    public int getPriority() {
        return priority;
    }

    @Override
    public Service buildService() {
        return new HttpService(url, statusCode);
    }

}
