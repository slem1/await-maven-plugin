package fr.sle.maven.plugins;

public class PollingConfig {

    private Integer attempts;

    private Integer sleep;

    public PollingConfig() {
        attempts = 3;
        sleep = 1000;
    }

    public PollingConfig(int attempts, int sleep){
        this.attempts = attempts;
        this.sleep = sleep;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public Integer getSleep() {
        return sleep;
    }

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
