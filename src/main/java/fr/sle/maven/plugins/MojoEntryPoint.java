package fr.sle.maven.plugins;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Mojo(name = "Await")
public class MojoEntryPoint extends AbstractMojo {

    private static final PollingConfig DEFAULT_POLLING_CONFIG = new PollingConfig();

    private static final Comparator<PollingTask> TASK_PRIORITY_COMPARATOR = new Comparator<PollingTask>() {
        @Override
        public int compare(PollingTask o1, PollingTask o2) {
            return Integer.compare(o1.getPriority(), o2.getPriority());
        }
    };

    @Parameter
    private List<TCPConfig> tcps;

    @Parameter
    private List<HttpConfig> httpz;

    @Parameter
    private  PollingConfig poll;

    //for unit testing
    public void setTcps(List<TCPConfig> tcps) {
        this.tcps = tcps;
    }

    //for unit testing
    public void setHttpz(List<HttpConfig> httpz) {
        this.httpz = httpz;
    }

    //for unit testing
    public void setPoll(PollingConfig pollingConfig){
        this.poll = pollingConfig;
    }

    public void execute() throws MojoFailureException, MojoExecutionException {

        List<MojoConnectionConfig> configs = new ArrayList<>();

        if (tcps != null) {
            configs.addAll(tcps);
        }

        if (httpz != null) {
            configs.addAll(httpz);
        }

        try {
            Set<PollingTask> tasks = toPollingTasks(configs);

            if (tasks.isEmpty()) {
                getLog().warn("No tasks found");
            } else {

                for (PollingTask task : tasks) {
                    task.run();
                }

            }
        } catch (IllegalArgumentException e) {
            throw new MojoFailureException(e.getMessage(), e);
        }
    }

    private Set<PollingTask> toPollingTasks(List<MojoConnectionConfig> configs) {

        Set<PollingTask> pollingTasks = new TreeSet<>(TASK_PRIORITY_COMPARATOR);

        PollingConfig pollingConfig = poll == null ?
                DEFAULT_POLLING_CONFIG : poll.validate();

        for (MojoConnectionConfig config : configs) {
            Service service = config.generateService();
            PollingTask task = new PollingTask(service,
                    pollingConfig.getAttempts(),
                    pollingConfig.getSleep(), config.getPriority());
            pollingTasks.add(task);
        }

        return pollingTasks;
    }
}
