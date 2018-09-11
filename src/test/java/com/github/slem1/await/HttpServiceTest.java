package com.github.slem1.await;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class HttpServiceTest {

    @Test
    public void shouldConnectAndGet200() throws IOException, ServiceUnavailableException {
        URL url = Mockito.mock(URL.class);
        HttpURLConnection urlConnection = Mockito.mock(HttpURLConnection.class);
        when(urlConnection.getResponseCode()).thenReturn(200);
        when(url.openConnection()).thenReturn(urlConnection);
        HttpService httpService = new HttpService(url, 200);
        httpService.execute();
    }

    @Test(expected = ServiceUnavailableException.class)
    public void shouldThrowServiceUnavailableException() throws IOException, ServiceUnavailableException {
        URL url = Mockito.mock(URL.class);

        Mockito.when(url.toString()).thenReturn("http://localhost");

        HttpURLConnection urlConnection = Mockito.mock(HttpURLConnection.class);
        when(urlConnection.getResponseCode()).thenReturn(403);
        when(url.openConnection()).thenReturn(urlConnection);
        HttpService httpService = new HttpService(url, 200);

        try {
            httpService.execute();
        } catch (ServiceUnavailableException e) {
            Assert.assertEquals("GET http://localhost --> response status code=403", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertContructorParamUrl() {
        new HttpService(null, 200);
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertContructorParamStatusCode() throws MalformedURLException {
        new HttpService(new URL("http://localhost"), null);
    }

    @Test
    public void assertToStringBehavior() throws MalformedURLException {
        URL url = new URL("http://localhost");
        HttpService httpService = new HttpService(url, 200);
        Assert.assertEquals("http://localhost", httpService.toString());
    }

    @Test(expected = ServiceUnavailableException.class)
    public void shouldHandleIOExceptionAsServiceUnavailableException() throws IOException, ServiceUnavailableException {
        URL url = Mockito.mock(URL.class);

        when(url.toString()).thenReturn("http://localhost");
        when(url.openConnection()).thenThrow(new IOException("Connection error"));
        HttpService httpService = new HttpService(url, 200);

        try {
            httpService.execute();
        } catch (ServiceUnavailableException e) {

            Assert.assertEquals(IOException.class, e.getCause().getClass());
            Assert.assertEquals("http://localhost is unreachable", e.getMessage());
            throw e;
        }
    }
}
