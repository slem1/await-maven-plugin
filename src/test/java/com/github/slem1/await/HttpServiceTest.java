package com.github.slem1.await;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class HttpServiceTest {

    @Test
    public void shouldConnectAndGet200_HTTP() throws IOException, ServiceUnavailableException {
        URL url = Mockito.mock(URL.class);
        HttpURLConnection urlConnection = Mockito.mock(HttpURLConnection.class);
        when(urlConnection.getResponseCode()).thenReturn(200);
        when(url.openConnection()).thenReturn(urlConnection);
        when(url.getProtocol()).thenReturn("http");
        HttpService httpService = new HttpService(url, 200, false);
        httpService.execute();
    }

    @Test
    public void shouldConnectAndGet200_HTTP_With_AuthInfo() throws IOException, ServiceUnavailableException {
        String authInfo = "user:pass";
        String expectedPropertyKey = "Authorization";
        String expectedPropertyValue = "Basic dXNlcjpwYXNz";
        URL url = Mockito.mock(URL.class);
        HttpURLConnection urlConnection = Mockito.mock(HttpURLConnection.class);
        when(urlConnection.getResponseCode()).thenReturn(200);
        when(url.openConnection()).thenReturn(urlConnection);
        when(url.getProtocol()).thenReturn("http");
        when(url.getUserInfo()).thenReturn(authInfo);
        HttpService httpService = new HttpService(url, 200, false);
        httpService.execute();
        Mockito.verify(urlConnection, times(1)).setRequestProperty(eq(expectedPropertyKey), eq(expectedPropertyValue));
    }

    @Test
    public void shouldConnectAndGet200_HTTPS_Skip_SSLCertVerification() throws IOException, ServiceUnavailableException {
        URL url = Mockito.mock(URL.class);
        HttpsURLConnection urlConnection = Mockito.mock(HttpsURLConnection.class);
        when(urlConnection.getResponseCode()).thenReturn(200);
        when(url.openConnection()).thenReturn(urlConnection);
        when(url.getProtocol()).thenReturn("https");
        HttpService httpService = new HttpService(url, 200, true);
        httpService.execute();
    }

    @Test
    public void shouldConnectAndGet200_HTTPS_With_SSLCertVerification() throws IOException, ServiceUnavailableException {
        URL url = Mockito.mock(URL.class);
        HttpsURLConnection urlConnection = Mockito.mock(HttpsURLConnection.class);
        when(urlConnection.getResponseCode()).thenReturn(200);
        when(url.openConnection()).thenReturn(urlConnection);
        when(url.getProtocol()).thenReturn("https");
        HttpService httpService = new HttpService(url, 200, false);
        httpService.execute();
    }

    @Test(expected = ServiceUnavailableException.class)
    public void shouldThrowServiceUnavailableException_HTTP() throws IOException, ServiceUnavailableException {
        URL url = Mockito.mock(URL.class);
        Mockito.when(url.toString()).thenReturn("http://localhost");

        HttpURLConnection urlConnection = Mockito.mock(HttpURLConnection.class);
        when(urlConnection.getResponseCode()).thenReturn(403);
        when(url.openConnection()).thenReturn(urlConnection);
        when(url.getProtocol()).thenReturn("http");
        HttpService httpService = new HttpService(url, 200, false);

        try {
            httpService.execute();
        } catch (ServiceUnavailableException e) {
            Assert.assertEquals("GET http://localhost --> response status code=403", e.getMessage());
            throw e;
        }
    }

    @Test(expected = ServiceUnavailableException.class)
    public void shouldThrowServiceUnavailableException_HTTPS_Skip_SSLCertVerification() throws IOException, ServiceUnavailableException {
        URL url = Mockito.mock(URL.class);
        Mockito.when(url.toString()).thenReturn("https://localhost");

        HttpsURLConnection urlConnection = Mockito.mock(HttpsURLConnection.class);
        when(urlConnection.getResponseCode()).thenReturn(403);
        when(url.openConnection()).thenReturn(urlConnection);
        when(url.getProtocol()).thenReturn("https");
        HttpService httpService = new HttpService(url, 200, true);

        try {
            httpService.execute();
        } catch (ServiceUnavailableException e) {
            Assert.assertEquals("GET https://localhost --> response status code=403", e.getMessage());
            throw e;
        }
    }

    @Test(expected = ServiceUnavailableException.class)
    public void shouldThrowServiceUnavailableException_HTTPS_With_SSLCertVerification() throws IOException, ServiceUnavailableException {
        URL url = Mockito.mock(URL.class);

        Mockito.when(url.toString()).thenReturn("https://localhost");

        HttpsURLConnection urlConnection = Mockito.mock(HttpsURLConnection.class);
        when(urlConnection.getResponseCode()).thenReturn(403);
        when(urlConnection.toString()).thenReturn("https://localhost");
        when(url.openConnection()).thenReturn(urlConnection);
        when(url.getProtocol()).thenReturn("https");
        HttpService httpService = new HttpService(url, 200, false);

        try {
            httpService.execute();
        } catch (ServiceUnavailableException e) {
            Assert.assertEquals("GET https://localhost --> response status code=403", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertContructorParamUrl_Skip_SSLCertVerification() {
        new HttpService(null, 200, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertContructorParamUrl_With_SSLCertVerification() {
        new HttpService(null, 200, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertContructorParamStatusCode_HTTP() throws MalformedURLException {
        new HttpService(new URL("http://localhost"), null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertContructorParamStatusCode_HTTPS_Skip_SSLCertVerification() throws MalformedURLException {
        new HttpService(new URL("https://localhost"), null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertContructorParamStatusCode_HTTPS_With_SSLCertVerification() throws MalformedURLException {
        new HttpService(new URL("https://localhost"), null, false);
    }

    @Test
    public void assertToStringBehavior_HTTP() throws MalformedURLException {
        URL url = new URL("http://localhost");
        HttpService httpService = new HttpService(url, 200, false);
        Assert.assertEquals("http://localhost", httpService.toString());
    }

    @Test
    public void assertToStringBehavior_HTTPS_Skip_SSLCertVerification() throws MalformedURLException {
        URL url = new URL("https://localhost");
        HttpService httpService = new HttpService(url, 200, true);
        Assert.assertEquals("https://localhost", httpService.toString());
    }

    @Test
    public void assertToStringBehavior_HTTPS_With_SSLCertVerification() throws MalformedURLException {
        URL url = new URL("https://localhost");
        HttpService httpService = new HttpService(url, 200, false);
        Assert.assertEquals("https://localhost", httpService.toString());
    }

    @Test(expected = ServiceUnavailableException.class)
    public void shouldHandleIOExceptionAsServiceUnavailableException_HTTP() throws IOException, ServiceUnavailableException {
        URL url = Mockito.mock(URL.class);

        when(url.toString()).thenReturn("http://localhost");
        when(url.getProtocol()).thenReturn("http");
        when(url.openConnection()).thenThrow(new IOException("Connection error"));
        HttpService httpService = new HttpService(url, 200, false);

        try {
            httpService.execute();
        } catch (ServiceUnavailableException e) {

            Assert.assertEquals(IOException.class, e.getCause().getClass());
            Assert.assertEquals("http://localhost is unreachable", e.getMessage());
            throw e;
        }
    }

    @Test(expected = ServiceUnavailableException.class)
    public void shouldHandleIOExceptionAsServiceUnavailableException_HTTPS_Skip_SSLCertVerification() throws IOException, ServiceUnavailableException {
        URL url = Mockito.mock(URL.class);

        when(url.toString()).thenReturn("https://localhost");
        when(url.getProtocol()).thenReturn("https");
        when(url.openConnection()).thenThrow(new IOException("Connection error"));
        HttpService httpService = new HttpService(url, 200, true);

        try {
            httpService.execute();
        } catch (ServiceUnavailableException e) {

            Assert.assertEquals(IOException.class, e.getCause().getClass());
            Assert.assertEquals("https://localhost is unreachable", e.getMessage());
            throw e;
        }
    }

    @Test(expected = ServiceUnavailableException.class)
    public void shouldHandleIOExceptionAsServiceUnavailableException_HTTPS_With_SSLCertVerification() throws IOException, ServiceUnavailableException {
        URL url = Mockito.mock(URL.class);

        when(url.toString()).thenReturn("https://localhost");
        when(url.getProtocol()).thenReturn("https");
        when(url.openConnection()).thenThrow(new IOException("Connection error"));
        HttpService httpService = new HttpService(url, 200, false);

        try {
            httpService.execute();
        } catch (ServiceUnavailableException e) {

            Assert.assertEquals(IOException.class, e.getCause().getClass());
            Assert.assertEquals("https://localhost is unreachable", e.getMessage());
            throw e;
        }
    }
}
