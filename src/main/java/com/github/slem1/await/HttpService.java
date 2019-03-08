package com.github.slem1.await;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Handler for testing connection to remote service on http.
 *
 * @author slemoine
 */
public class HttpService implements Service {

    private final URL url;

    private final Integer statusCode;

    private final boolean trustAllCerts;

    /**
     * Creates a new instance based on {@code url}. The expected response status code
     * is {@code statusCode}.
     *
     * @param url        the url of the service to connect to.
     * @param statusCode the expected http response status code.
     */
    public HttpService(URL url, Integer statusCode, boolean trustAllCerts) {

        if (url == null) {
            throw new IllegalArgumentException("URL is mandatory");
        }

        if (statusCode == null) {
            throw new IllegalArgumentException("status code is mandatory");
        }

        this.url = url;
        this.statusCode = statusCode;
        this.trustAllCerts = trustAllCerts;
    }

    private TrustManager[] getTrustAllCertsTrustManager() {
        return new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
    }

    @Override
    public String toString() {
        return url.toString();
    }

    @Override
    public void execute() throws ServiceUnavailableException {
        try {
            HttpURLConnection urlConnection;
            if (url.getProtocol().equals("https")) {
                urlConnection = (HttpsURLConnection) url.openConnection();
                if (trustAllCerts) {
                    SSLContext sc = SSLContext.getInstance("SSL");
                    sc.init(null, getTrustAllCertsTrustManager(), new java.security.SecureRandom());
                    ((HttpsURLConnection) urlConnection).setSSLSocketFactory(sc.getSocketFactory());
                }
            } else {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("method", "GET");
            }
            urlConnection.connect();

            if (urlConnection.getResponseCode() != statusCode) {
                throw new ServiceUnavailableException(String.format("GET %s --> response status code=%d", url.toString(), urlConnection.getResponseCode()));
            }
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new IllegalStateException("Failed to init SSL context: ", e);
        } catch (IOException e) {
            throw new ServiceUnavailableException(url + " is unreachable", e);
        }
    }
}
