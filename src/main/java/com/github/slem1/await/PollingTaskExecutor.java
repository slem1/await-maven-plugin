package com.github.slem1.await;

import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

public class PollingTaskExecutor {

    public void run(List<PollingTask> tasks) throws MojoFailureException, MojoExecutionException {
        for (PollingTask task : tasks) {
            task.run();
        }
    }

}
