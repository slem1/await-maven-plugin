package com.github.slem1.await;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InOrder;
import org.mockito.Mockito;

@RunWith(JUnit4.class)
public class MojoEntryPointTest {

    private static final PollingConfig POLLING_TEST_CONFIG = new PollingConfig(3, 1);

    private MojoEntryPoint mojoEntryPoint;

    @Before
    public void init() {
        mojoEntryPoint = new MojoEntryPoint();
        mojoEntryPoint.setPoll(POLLING_TEST_CONFIG);
    }

    @Test
    public void shouldExitProperly() throws MojoFailureException, MojoExecutionException, ServiceUnavailableException {

        TCPConnectionConfig tcpConnectionConfig = mock(TCPConnectionConfig.class);
        Service service = mock(Service.class);
        when(tcpConnectionConfig.buildService()).thenReturn(service);

        mojoEntryPoint.setTcpConnections(Collections.singletonList(tcpConnectionConfig));
        mojoEntryPoint.execute();

        verify(service).execute();

    }

    @Test
    public void shouldExitProperlyIfNothingToDo() throws MojoFailureException, MojoExecutionException {
        MojoEntryPoint mojoEntryPoint = new MojoEntryPoint();
        mojoEntryPoint.execute();
    }

    @Test(expected = MojoFailureException.class)
    public void shouldThrowOnUnreachableService() throws MojoFailureException, MojoExecutionException, ServiceUnavailableException {

        TCPConnectionConfig tcpConnectionConfig = mock(TCPConnectionConfig.class);
        Service service = mock(Service.class);
        when(service.toString()).thenReturn("localhost:10080 (TCP)");
        doThrow(new ServiceUnavailableException("Service unavailable")).when(service).execute();
        when(tcpConnectionConfig.buildService()).thenReturn(service);

        mojoEntryPoint.setTcpConnections(Collections.singletonList(tcpConnectionConfig));

        try {
            mojoEntryPoint.execute();
        } catch (MojoFailureException e) {
            verify(service, times(3)).execute();
            Assert.assertEquals("Service unreachable after 3 attempts: localhost:10080 (TCP)", e.getMessage());
            throw e;
        }
    }

    @Test(expected = MojoFailureException.class)
    public void shouldRunTasksInOrder() throws MojoFailureException, MojoExecutionException, ServiceUnavailableException {

        TCPConnectionConfig tcpConnectionConfig = mock(TCPConnectionConfig.class);
        when(tcpConnectionConfig.getPriority()).thenReturn(1);
        Service tcpService = mock(Service.class);
        when(tcpService.toString()).thenReturn("localhost:10080 (TCP)");
        doThrow(new ServiceUnavailableException("Service unavailable")).when(tcpService).execute();
        when(tcpConnectionConfig.buildService()).thenReturn(tcpService);

        HttpConnectionConfig httpConnectionConfig = mock(HttpConnectionConfig.class);
        when(httpConnectionConfig.getPriority()).thenReturn(0);
        Service httpService = mock(HttpService.class);
        when(httpService.toString()).thenReturn("http://localhost:10080");
        when(httpConnectionConfig.buildService()).thenReturn(httpService);

        mojoEntryPoint.setTcpConnections(Collections.singletonList(tcpConnectionConfig));
        mojoEntryPoint.setHttpConnections(Collections.singletonList(httpConnectionConfig));

        try {
            mojoEntryPoint.execute();
        } catch (MojoFailureException e) {
            verify(httpService).execute();
            verify(tcpService, times(3)).execute();
            Assert.assertEquals("Service unreachable after 3 attempts: localhost:10080 (TCP)", e.getMessage());
            throw e;
        }
    }

    @Test
    public void shouldRunTasksInOrderIfNoPriority() throws MojoFailureException, MojoExecutionException, ServiceUnavailableException {

        TCPConnectionConfig tcpConnectionConfig = mock(TCPConnectionConfig.class);
        Service tcpService = mock(Service.class);
        when(tcpService.toString()).thenReturn("localhost:10080 (TCP)");
        when(tcpConnectionConfig.buildService()).thenReturn(tcpService);

        HttpConnectionConfig httpConnectionConfig = mock(HttpConnectionConfig.class);
        Service httpService = mock(HttpService.class);
        when(httpService.toString()).thenReturn("http://localhost:10080");
        when(httpConnectionConfig.buildService()).thenReturn(httpService);

        mojoEntryPoint.setTcpConnections(Collections.singletonList(tcpConnectionConfig));
        mojoEntryPoint.setHttpConnections(Collections.singletonList(httpConnectionConfig));
        mojoEntryPoint.execute();

        InOrder inOrder = Mockito.inOrder(tcpService, httpService);

        inOrder.verify(tcpService).execute();
        inOrder.verify(httpService).execute();
    }

    @Test
    public void shouldRunTasksInOrderWithPriority() throws MojoFailureException, MojoExecutionException, ServiceUnavailableException {

        TCPConnectionConfig tcpConnectionConfig1 = mock(TCPConnectionConfig.class);
        Service tcpService1 = mock(Service.class);
        when(tcpService1.toString()).thenReturn("localhost:10080 (TCP-1)");
        when(tcpConnectionConfig1.buildService()).thenReturn(tcpService1);
        when(tcpConnectionConfig1.getPriority()).thenReturn(100);

        HttpConnectionConfig httpConnectionConfig = mock(HttpConnectionConfig.class);
        Service httpService = mock(HttpService.class);
        when(httpService.toString()).thenReturn("http://localhost:10080");
        when(httpConnectionConfig.buildService()).thenReturn(httpService);
        when(httpConnectionConfig.getPriority()).thenReturn(50);

        TCPConnectionConfig tcpConnectionConfig2 = mock(TCPConnectionConfig.class);
        Service tcpService2 = mock(Service.class);
        when(tcpService2.toString()).thenReturn("localhost:10081 (TCP-2)");
        when(tcpConnectionConfig2.buildService()).thenReturn(tcpService2);
        when(tcpConnectionConfig2.getPriority()).thenReturn(Integer.MAX_VALUE);

        mojoEntryPoint.setTcpConnections(Arrays.asList(tcpConnectionConfig1, tcpConnectionConfig2));
        mojoEntryPoint.setHttpConnections(Collections.singletonList(httpConnectionConfig));
        mojoEntryPoint.execute();

        InOrder inOrder = Mockito.inOrder(tcpService2, tcpService1, httpService);

        inOrder.verify(httpService).execute();
        inOrder.verify(tcpService1).execute();
        inOrder.verify(tcpService2).execute();

    }

    @Test(expected = MojoFailureException.class)
    public void shouldHandleIllegalArgumentToMojoFailure() throws MojoFailureException, MojoExecutionException {

        TCPConnectionConfig tcpConnectionConfig = mock(TCPConnectionConfig.class);
        when(tcpConnectionConfig.getPriority()).thenReturn(1);
        mojoEntryPoint.setTcpConnections(Collections.singletonList(tcpConnectionConfig));

        try {
            mojoEntryPoint.execute();

        } catch (MojoFailureException e) {
            Throwable cause = e.getCause();
            Assert.assertEquals(cause.getClass(), IllegalArgumentException.class);
            Assert.assertEquals("Service is mandatory", e.getCause().getMessage());
            throw e;
        }
    }

    @Test
    public void shouldSkip() throws NoSuchFieldException, IllegalAccessException, MojoFailureException, MojoExecutionException {

        MojoEntryPoint mojoEntryPoint = new MojoEntryPoint();
        PollingTaskExecutor taskExecutorMock = mock(PollingTaskExecutor.class);

        HttpConnectionConfig httpConnectionConfig = mock(HttpConnectionConfig.class);
        when(httpConnectionConfig.getPriority()).thenReturn(0);
        Service httpService = mock(HttpService.class);
        when(httpService.toString()).thenReturn("http://localhost:10080");
        when(httpConnectionConfig.buildService()).thenReturn(httpService);

        Field pollingTaskExecutor = MojoEntryPoint.class.getDeclaredField("pollingTaskExecutor");
        pollingTaskExecutor.setAccessible(true);
        pollingTaskExecutor.set(mojoEntryPoint, taskExecutorMock);
        mojoEntryPoint.setAwaitSkip(true);
        mojoEntryPoint.setHttpConnections(Collections.singletonList(httpConnectionConfig));
        mojoEntryPoint.execute();

        Mockito.verifyZeroInteractions(taskExecutorMock);
    }
}
