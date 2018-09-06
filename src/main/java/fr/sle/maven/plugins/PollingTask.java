package fr.sle.maven.plugins;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

/**
 * Adds the polling logic to a {@link Service} execution
 *
 * @author slemoine
 */
class PollingTask {

    private static Log log = new SystemStreamLog();

    private final int maxAttempt;

    private final int waitTime;

    private final int priority;

    private final Service service;

    PollingTask(final Service service, int maxAttempt, final int waitTime, final int priority) {

        if (service == null) {
            throw new IllegalArgumentException("Service is mandatory");
        }

        if (maxAttempt < 1) {
            throw new IllegalArgumentException("Polling task should executes service once at least");
        }

        this.service = service;
        this.maxAttempt = maxAttempt;
        this.waitTime = waitTime;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    void run() throws MojoExecutionException, MojoFailureException {

        int n = 0;

        do {
            try {
                service.execute();
                log.info(service + " is available");
                return;
            } catch (ServiceUnavailableException e) {
                log.info(e.getMessage());
                n++;
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e1) {
                    throw new MojoExecutionException("Unexpected thread exception", e1);
                }
            }

        } while (n < maxAttempt);

        throw new MojoFailureException(String.format("Service unreachable after %d attempts: %s",
                maxAttempt, service));

    }

}
