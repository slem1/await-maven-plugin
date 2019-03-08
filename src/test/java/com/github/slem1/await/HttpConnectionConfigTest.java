package com.github.slem1.await;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.net.MalformedURLException;
import java.net.URL;

@RunWith(JUnit4.class)
public class HttpConnectionConfigTest {

    @Test
    public void shouldCreateDefaultInstance() {
        HttpConnectionConfig httpConnectionConfig = new HttpConnectionConfig();
        Assert.assertEquals(Integer.MAX_VALUE,httpConnectionConfig.getPriority());
    }

    @Test
    public void shouldCreateInitializedInstance() throws MalformedURLException {
        HttpConnectionConfig httpConnectionConfig = new HttpConnectionConfig(new URL("http://localhost:9000"), 200, 0, true);
        Assert.assertEquals(0, httpConnectionConfig.getPriority());
        Service service = httpConnectionConfig.buildService();
        Assert.assertEquals(HttpService.class, service.getClass());
    }
}
