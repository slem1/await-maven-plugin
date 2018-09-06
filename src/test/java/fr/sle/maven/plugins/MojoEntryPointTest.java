package fr.sle.maven.plugins;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class MojoEntryPointTest {

    private static final PollingConfig POLLING_TEST_CONFIG = new PollingConfig(3, 1);

    private MojoEntryPoint mojoEntryPoint;

    @Before
    public void init(){
        mojoEntryPoint = new MojoEntryPoint();
        mojoEntryPoint.setPoll(POLLING_TEST_CONFIG);
    }

    @Test
    public void shouldExitProperly() throws MojoFailureException, MojoExecutionException, ServiceUnavailableException {

        TCPConfig tcpConfig = mock(TCPConfig.class);
        Service service = mock(Service.class);
        when(tcpConfig.generateService()).thenReturn(service);

        mojoEntryPoint.setTcps(Collections.singletonList(tcpConfig));
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

        TCPConfig tcpConfig = mock(TCPConfig.class);
        Service service = mock(Service.class);
        when(service.toString()).thenReturn("localhost:10080 (TCP)");
        doThrow(new ServiceUnavailableException("Service unavailable")).when(service).execute();
        when(tcpConfig.generateService()).thenReturn(service);

        mojoEntryPoint.setTcps(Collections.singletonList(tcpConfig));

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

        TCPConfig tcpConfig = mock(TCPConfig.class);
        when(tcpConfig.getPriority()).thenReturn(1);
        Service tcpService = mock(Service.class);
        when(tcpService.toString()).thenReturn("localhost:10080 (TCP)");
        doThrow(new ServiceUnavailableException("Service unavailable")).when(tcpService).execute();
        when(tcpConfig.generateService()).thenReturn(tcpService);

        HttpConfig httpConfig = mock(HttpConfig.class);
        when(httpConfig.getPriority()).thenReturn(0);
        Service httpService = mock(HttpService.class);
        when(httpService.toString()).thenReturn("http://localhost:10080");
        when(httpConfig.generateService()).thenReturn(httpService);

        mojoEntryPoint.setTcps(Collections.singletonList(tcpConfig));
        mojoEntryPoint.setHttpz(Collections.singletonList(httpConfig));

        try {
            mojoEntryPoint.execute();
        } catch (MojoFailureException e) {
            verify(httpService).execute();
            verify(tcpService, times(3)).execute();
            Assert.assertEquals("Service unreachable after 3 attempts: localhost:10080 (TCP)", e.getMessage());
            throw e;
        }
    }

    @Test(expected = MojoFailureException.class)
    public void shouldHandleIllegalArgumentToMojoFailure() throws MojoFailureException, MojoExecutionException {

        TCPConfig tcpConfig = mock(TCPConfig.class);
        when(tcpConfig.getPriority()).thenReturn(1);
        mojoEntryPoint.setTcps(Collections.singletonList(tcpConfig));

        try {
            mojoEntryPoint.execute();

        } catch (MojoFailureException e) {
            Throwable cause = e.getCause();
            Assert.assertEquals(cause.getClass(), IllegalArgumentException.class);
            Assert.assertEquals("Service is mandatory", e.getCause().getMessage());
            throw e;
        }

    }
}
