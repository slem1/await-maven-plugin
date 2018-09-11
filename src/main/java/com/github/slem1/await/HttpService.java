package com.github.slem1.await;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Handler for testing connection to remote service on http.
 *
 * @author slemoine
 */
public class HttpService implements Service {

    private final URL url;

    private final Integer statusCode;

    /**
     * Creates a new instance based on {@code url}. The expected response status code
     * is {@code statusCode}.
     *
     * @param url        the url of the service to connect to.
     * @param statusCode the expected http response status code.
     */
    public HttpService(URL url, Integer statusCode) {

        if (url == null) {
            throw new IllegalArgumentException("URL is mandatory");
        }

        if (statusCode == null) {
            throw new IllegalArgumentException("status code is mandatory");
        }

        this.url = url;
        this.statusCode = statusCode;

    }

    @Override
    public String toString() {
        return url.toString();
    }

    @Override
    public void execute() throws ServiceUnavailableException {

        try {

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("method", "GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() != statusCode) {
                throw new ServiceUnavailableException(String.format("GET %s --> response status code=%d", url.toString(), urlConnection.getResponseCode()));
            }

        } catch (IOException e) {
            throw new ServiceUnavailableException(url + " is unreachable", e);
        }
    }
}
