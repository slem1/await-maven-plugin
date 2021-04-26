package com.github.slem1.await;


import java.io.IOException;
import java.net.ServerSocket;

import javax.net.ServerSocketFactory;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TCPServiceTest {

    @Test
    public void shouldExecute() throws IOException, ServiceUnavailableException {
        try (ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(0)) {
            int port = serverSocket.getLocalPort();
            TCPService tcpServiceChecker = new TCPService("localhost", port);
            tcpServiceChecker.execute();
        }
    }

    @Test(expected = ServiceUnavailableException.class)
    public void shouldThrowServiceUnreachableException() throws IOException, ServiceUnavailableException {

        try (ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(0)) {
            int port = serverSocket.getLocalPort();
            serverSocket.close();

            TCPService tcpServiceChecker = new TCPService("localhost", port);
            tcpServiceChecker.execute();
        }
    }

    @Test
    public void assertToString(){
        TCPService tcpService = new TCPService("localhost", 80);
        Assert.assertEquals("localhost:80 (TCP)", tcpService.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCheckContructorArgsHost(){
        new TCPService(null, 1034);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCheckContructorArgsPort(){
        new TCPService("localhost", null);
    }

}
