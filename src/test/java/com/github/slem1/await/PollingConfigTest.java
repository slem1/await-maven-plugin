package com.github.slem1.await;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PollingConfigTest {

    @Test
    public void shouldCreateInstanceWithDefaultValue() {
        PollingConfig pollingConfig = new PollingConfig();
        Assert.assertEquals(3, pollingConfig.getAttempts());
        Assert.assertEquals(1000, pollingConfig.getSleep());
    }

    @Test
    public void shouldCreateInstanceWithUserValue() {
        PollingConfig pollingConfig = new PollingConfig(10, 2000);
        Assert.assertEquals(10, pollingConfig.getAttempts());
        Assert.assertEquals(2000, pollingConfig.getSleep());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIfMaxAttemptLowerThan1() {
        try {
            new PollingConfig(0, 2000);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("attempts must be >= 1", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIfSleepIsNegative() {
        try {
            new PollingConfig(10, -1);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("sleep cannot be negative", e.getMessage());
            throw e;
        }
    }

    @Test
    public void runValidate() {
        PollingConfig pollingConfig = new PollingConfig();
        PollingConfig result = pollingConfig.validate();
        Assert.assertEquals(result, pollingConfig);
    }
}
