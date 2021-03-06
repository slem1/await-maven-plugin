package com.github.slem1.await;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TCPConnectionConfigTest {

    @Test
    public void shouldCreateDefaultInstance() {
        TCPConnectionConfig tcpConnectionConfig = new TCPConnectionConfig();
        Assert.assertEquals(Integer.MAX_VALUE,tcpConnectionConfig.getPriority());
    }

    @Test
    public void shouldCreateInitializedInstance() {
        TCPConnectionConfig tcpConnectionConfig = new TCPConnectionConfig("localhost", 9090, 0);
        Assert.assertEquals(0, tcpConnectionConfig.getPriority());
        Service service = tcpConnectionConfig.buildService();
        Assert.assertEquals(TCPService.class, service.getClass());
    }
}
