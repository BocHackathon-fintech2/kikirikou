package io.kikirikou.modules.common.managers.impl;


import org.apache.tapestry5.ioc.internal.services.cron.PeriodicExecutorImpl;
import org.apache.tapestry5.ioc.services.ParallelExecutor;
import org.apache.tapestry5.ioc.services.cron.Schedule;
import org.slf4j.Logger;


public class CustomPeriodicExecutorImpl extends PeriodicExecutorImpl {
    private final Logger logger;

    public CustomPeriodicExecutorImpl(ParallelExecutor parallelExecutor, Logger logger) {
        super(parallelExecutor, logger);
        this.logger = logger;
    }

    @Override
    public org.apache.tapestry5.ioc.services.cron.PeriodicJob addJob(Schedule schedule, String name, Runnable job) {
        return super.addJob(schedule, name, () -> {
            try {
                job.run();
            } catch (Throwable ex) {
                logger.error("Error on periodic job: " + name, ex);
            }
        });
    }
}
