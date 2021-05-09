package com.github.slem1.await;

/**
 * The polling configuration which allows to configure {@link PollingTask}.
 *
 * @author slemoine
 */
public class PollingConfig {

    private int attempts = 3;

    private int sleep = 1000;

    private boolean throwOnFail = true;
    /**
     * Default constructor used by maven
     */
    public PollingConfig() {
    }

    /**
     * Initialize the polling configuration with {@code attempts} and {@code sleep} waiting time in milliseconds between
     * two attempts.
     *
     * @param attempts the max number of connection attempts.
     * @param sleep    the waiting time in ms.
     * @param throwOnFail true to throw if all attempts are unsuccessfull, false will just log it 
     * 
     */
    public PollingConfig(int attempts, int sleep, boolean throwOnFail) {
        validate(attempts, sleep);
        this.attempts = attempts;
        this.sleep = sleep;
        this.throwOnFail = throwOnFail;
    }

    /**
     * Return the number of attempt.
     *
     * @return the number of attempts.
     */
    public int getAttempts() {
        return attempts;
    }

    /**
     * Return the waiting time between two attempts of connection.
     *
     * @return the waiting time
     */
    public int getSleep() {
        return sleep;
    }

    /**
     * Return if we should throw exception when all attempts fail.
     *
     * @return true if we should throw exception
     */
    public boolean isThrowOnFail() {
        return throwOnFail;
    }

    /**
     * Validates this configuration.
     *
     * @return this configuration.
     * @throws IllegalArgumentException if the configuration is invalid.
     */
    public PollingConfig validate() {
        validate(attempts, sleep);
        return this;
    }

    private void validate(int attempts, int sleep) {
        if (attempts < 1) {
            throw new IllegalArgumentException("attempts must be >= 1");
        }

        if (sleep < 0) {
            throw new IllegalArgumentException("sleep cannot be negative");
        }
    }
}
