package io.kikirikou.modules.common.managers.impl;


import io.kikirikou.modules.common.managers.decl.StartupManager;
import org.slf4j.Logger;

import java.util.List;

public class StartupManagerImpl implements StartupManager {
    private final List<Runnable> configuration;
    private final Logger logger;

    public StartupManagerImpl(Logger logger, List<Runnable> configuration) {
        this.logger = logger;
        this.configuration = configuration;
    }

    @Override
    public int startup() {
        configuration.forEach(runnable -> {
            try {
                runnable.run();
            } catch (Throwable ex) {
                logger.error("Error running startup job", ex);
                throw ex;
            }
        });

        return configuration.size();
    }
}
