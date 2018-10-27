package io.kikirikou.modules.persistence.managers.impl;

import io.kikirikou.modules.common.utils.AssertUtils;
import io.kikirikou.modules.persistence.managers.decl.NotificationListenerManager;
import io.kikirikou.modules.persistence.other.NotificationListener;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.apache.tapestry5.json.JSONObject;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class NotificationListenerManagerImpl implements NotificationListenerManager {
    private final DataSource dataSource;
    private final List<Runnable> jobs;
    private final ExecutorService executorService;
    private final PerthreadManager perthreadManager;
    private final Logger logger;

    public NotificationListenerManagerImpl(DataSource dataSource,
                                           RegistryShutdownHub registryShutdownHub,
                                           ExecutorService executorService,
                                           PerthreadManager perthreadManager,
                                           Logger logger) {
        this.dataSource = dataSource;
        this.logger = logger;
        this.jobs = CollectionFactory.newThreadSafeList();
        this.executorService = executorService;
        this.perthreadManager = perthreadManager;
        registryShutdownHub.addRegistryShutdownListener((Runnable) () -> jobs.forEach(Runnable::run));
    }

    @Override
    public Runnable listen(String channel, NotificationListener listener) {
        AssertUtils.hasText(channel, "channel");
        AssertUtils.notNull(listener, "listener");

        Future<?> submit = executorService.submit(() -> {
            Connection connection = null;
            try {
                connection = dataSource.getConnection();
                Statement stmt = connection.createStatement();
                stmt.execute("LISTEN \"" + channel + "\"");
                stmt.close();
                PGConnection pgConnection = connection.unwrap(PGConnection.class);

                while (!Thread.currentThread().isInterrupted()) {
                    PGNotification notifications[] = pgConnection.getNotifications();
                    if (notifications != null) {
                        for (PGNotification notification : notifications)
                            try {
                                listener.accept(channel, new JSONObject(notification.getParameter()));
                            }catch(Throwable ex) {
                                logger.error("Listener threw exception",ex);
                            }
                        perthreadManager.cleanup();
                    }

                    Thread.sleep(500);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            try {
                Statement stmt = connection.createStatement();
                stmt.execute("UNLISTEN \"" + channel + "\"");
                stmt.close();
            } catch (Throwable ignored) {
            }
        });

        Runnable job = new Runnable() {
            @Override
            public void run() {
                try {
                    submit.cancel(true);
                } finally {
                    jobs.remove(this);
                }
            }
        };
        jobs.add(job);
        return job;
    }
}
