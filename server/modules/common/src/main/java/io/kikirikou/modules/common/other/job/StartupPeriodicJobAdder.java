package io.kikirikou.modules.common.other.job;

import org.apache.tapestry5.ioc.services.cron.PeriodicExecutor;

public class StartupPeriodicJobAdder implements Runnable {
    private final PeriodicExecutor periodicExecutor;
    private final PeriodicJob job;

    public StartupPeriodicJobAdder(PeriodicExecutor periodicExecutor, PeriodicJob job) {
        this.periodicExecutor = periodicExecutor;
        this.job = job;
    }

    @Override
    public void run() {
        periodicExecutor.addJob(job.getSchedule(), job.getName(), job.getJob());
    }
}