package io.kikirikou.modules.common.other.job;

import org.apache.tapestry5.ioc.services.cron.Schedule;

public interface PeriodicJob {
    Schedule getSchedule();

    String getName();

    Runnable getJob();
}