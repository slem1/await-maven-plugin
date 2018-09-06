package fr.sle.maven.plugins;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public class PollingTaskTest {


    @Test(expected = MojoFailureException.class)
    public void shouldThrowServiceUnreachable() throws MojoFailureException, MojoExecutionException {

        Service service = new Service() {
            @Override
            public void execute() throws ServiceUnavailableException {
                throw new ServiceUnavailableException("Service is unreachable", new IOException());
            }

            @Override
            public String toString() {
                return "tcp://localhost:5000";
            }
        };

        PollingTask pollingTask = new PollingTask(service, 3, 1, 0);

        try {
            pollingTask.run();
        } catch (MojoFailureException e) {
            Assert.assertEquals("Service unreachable after 3 attempts: " + service, e.getMessage());
            throw e;
        }
    }

    @Test
    public void shouldReachServiceAfterTwoAttempts() throws MojoFailureException, MojoExecutionException {

        Service service = new Service() {

            int counter = 1;

            @Override
            public void execute() throws ServiceUnavailableException {
                if (counter++ < 2) {
                    throw new ServiceUnavailableException("Service is unreachable", new IOException());
                }
            }

            @Override
            public String toString() {
                return "tcp://localhost:5000";
            }
        };

        PollingTask pollingTask = new PollingTask(service, 3, 1, 0);

        pollingTask.run();
    }

    @Test
    public void shouldOrderTaskByPriority(){



    }
}
