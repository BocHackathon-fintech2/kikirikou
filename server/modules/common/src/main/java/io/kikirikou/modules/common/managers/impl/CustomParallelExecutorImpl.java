package io.kikirikou.modules.common.managers.impl;

import org.apache.tapestry5.ioc.internal.services.ParallelExecutorImpl;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.apache.tapestry5.ioc.services.ThunkCreator;

import java.util.concurrent.ExecutorService;

public class CustomParallelExecutorImpl extends ParallelExecutorImpl {
    public CustomParallelExecutorImpl(ExecutorService executorService, ThunkCreator thunkCreator, PerthreadManager perthreadManager) {
        super(executorService, thunkCreator, perthreadManager);
    }
}
