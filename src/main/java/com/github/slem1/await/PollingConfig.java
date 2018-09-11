package com.github.slem1.await;

/**
 * The polling configuration which allows to configure {@link PollingTask}.
 *
 * @author slemoine
 */
public class PollingConfig {

    private Integer attempts;

    private Integer sleep;

    /**
     * Initialize a default polling configuration with default values.
     */
    public PollingConfig() {
        attempts = 3;
        sleep = 1000;
    }

    /**
     * Initialize the polling configuration with {@code attempts} and {@code sleep} waiting time in milliseconds between
     * two attempts.
     *
     * @param attempts the max number of connection attempts.
     * @param sleep    the waiting time in ms.
     */
    public PollingConfig(int attempts, int sleep) {
        this.attempts = attempts;
        this.sleep = sleep;
    }

    /**
     * Return the number of attempt.
     *
     * @return the number of attempts.
     */
    public Integer getAttempts() {
        return attempts;
    }

    /**
     * Return the waiting time between two attempts of connection.
     *
     * @return the waiting time
     */
    public Integer getSleep() {
        return sleep;
    }

    /**
     * Validates this configuration.
     *
     * @return this configuration.
     * @throws IllegalArgumentException if the configuration is invalid.
     */
    public PollingConfig validate() {
        if (attempts == null || attempts < 1) {
            throw new IllegalArgumentException("attempts is mandatory and must be >= 1");
        }

        if (sleep == null || sleep < 0) {
            throw new IllegalArgumentException("sleep is mandatory and must be >= 0");
        }

        return this;
    }
}
