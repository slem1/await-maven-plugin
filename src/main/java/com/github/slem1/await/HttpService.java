package com.github.slem1.await;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Handler for testing connection to remote service on http.
 *
 * @author slemoine
 */
public class HttpService implements Service {

    private static final String HTTPS = "https";
    private static final String SSL = "SSL";

    private static final TrustManager[] ignoreSSLCertTrustManager;

    private final URL url;

    private final Integer statusCode;

    private final boolean skipSSLCertVerification;

    static {
        ignoreSSLCertTrustManager =  new TrustManager[]{
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

    /**
     * Creates a new instance based on {@code url}. The expected response status code
     * is {@code statusCode}.
     *
     * @param url        the url of the service to connect to.
     * @param statusCode the expected http response status code.
     */
    public HttpService(URL url, Integer statusCode, boolean skipSSLCertVerification) {

        if (url == null) {
            throw new IllegalArgumentException("URL is mandatory");
        }

        if (statusCode == null) {
            throw new IllegalArgumentException("status code is mandatory");
        }

        this.url = url;
        this.statusCode = statusCode;
        this.skipSSLCertVerification = skipSSLCertVerification;
    }

    @Override
    public String toString() {
        return url.toString();
    }

    @Override
    public void execute() throws ServiceUnavailableException {
        try {
            HttpURLConnection urlConnection;
            if (url.getProtocol().equals(HTTPS)) {
                urlConnection = (HttpsURLConnection) url.openConnection();
                if (skipSSLCertVerification) {
                    SSLContext sc = SSLContext.getInstance(SSL);
                    sc.init(null, ignoreSSLCertTrustManager, new java.security.SecureRandom());
                    ((HttpsURLConnection) urlConnection).setSSLSocketFactory(sc.getSocketFactory());
                }
            } else {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("method", "GET");
                if(null != url.getUserInfo()){
                    urlConnection.setRequestProperty("Authorization",
                        String.format("Basic %s", DatatypeConverter.printBase64Binary(
                            url.getUserInfo().getBytes(StandardCharsets.UTF_8)
                    )));
                }
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
