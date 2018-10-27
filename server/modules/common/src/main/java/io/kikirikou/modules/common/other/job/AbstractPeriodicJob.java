package io.kikirikou.modules.common.other.job;

import org.apache.tapestry5.ioc.services.cron.Schedule;

public abstract class AbstractPeriodicJob implements PeriodicJob, Runnable {
    private final Schedule schedule;

    public AbstractPeriodicJob(Schedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public Schedule getSchedule() {
        return schedule;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Runnable getJob() {
        return this;
    }

    @Override
    public abstract void run();
}