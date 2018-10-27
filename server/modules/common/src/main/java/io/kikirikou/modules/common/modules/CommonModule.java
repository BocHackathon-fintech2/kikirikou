package io.kikirikou.modules.common.modules;

import io.kikirikou.modules.common.coercers.*;
import io.kikirikou.modules.common.managers.decl.*;
import io.kikirikou.modules.common.managers.impl.*;
import io.kikirikou.modules.common.markers.Common;
import io.kikirikou.modules.common.other.CommonSymbolConstants;
import io.kikirikou.modules.common.other.executorservice.CleanupExecutorService;
import io.kikirikou.modules.common.other.executorservice.DirectExecutionService;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.ioc.services.*;
import org.apache.tapestry5.ioc.services.cron.PeriodicExecutor;
import org.apache.tapestry5.ioc.util.TimeInterval;
import org.slf4j.Logger;

import java.net.InetAddress;
import java.nio.file.Path;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Marker(Common.class)
public class CommonModule {
    @Startup
    public static void startup(StartupManager startupManager, Logger logger) {
        logger.info("Executed {} startup functions", startupManager.startup());
    }

    @Contribute(SymbolProvider.class)
    @FactoryDefaults
    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
        configuration.add(CommonSymbolConstants.PRODUCTION_MODE, true);
    }


    public static void bind(ServiceBinder binder) {
        binder.bind(StartupManager.class, StartupManagerImpl.class);
        binder.bind(EnvironmentalShadowBuilder.class, EnvironmentalShadowBuilderImpl.class);
        binder.bind(PeriodicExecutor.class, CustomPeriodicExecutorImpl.class).
                withSimpleId();
        binder.bind(ParallelExecutor.class, CustomParallelExecutorImpl.class).
                withSimpleId();
        binder.bind(StringToDataSourceManager.class, StringToDataSourceManagerImpl.class);
        binder.bind(FileMonitor.class, FileMonitorImpl.class);
    }

    @Contribute(ServiceOverride.class)
    public static void contributeServiceOverride(MappedConfiguration<Class, Object> configuration,
                                                 @Local PeriodicExecutor periodicExecutor,
                                                 @Local ParallelExecutor parallelExecutor) {
        configuration.add(PeriodicExecutor.class, periodicExecutor);
        configuration.add(ParallelExecutor.class, parallelExecutor);
    }

    public static ExecutorService buildExecutorService(RegistryShutdownHub registryShutdownHub,
                                                       PerthreadManager perthreadManager,
                                                       @Symbol(IOCSymbols.THREAD_POOL_CORE_SIZE) int coreSize,
                                                       @Symbol(IOCSymbols.THREAD_POOL_MAX_SIZE) int maxSize,
                                                       @Symbol(IOCSymbols.THREAD_POOL_KEEP_ALIVE)
                                                       @IntermediateType(TimeInterval.class) int keepAliveMillis,
                                                       @Symbol(IOCSymbols.THREAD_POOL_ENABLED) boolean threadPoolEnabled,
                                                       @Symbol(IOCSymbols.THREAD_POOL_QUEUE_SIZE) int queueSize) {


        ExecutorService executionService;
        if (!threadPoolEnabled)
            executionService = new DirectExecutionService();
        else {

            LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(queueSize);
            executionService = new CleanupExecutorService(new ThreadPoolExecutor(coreSize, maxSize, keepAliveMillis,
                    TimeUnit.MILLISECONDS, workQueue), perthreadManager);
        }

        registryShutdownHub.addRegistryShutdownListener((Runnable) executionService::shutdownNow);

        return executionService;
    }

    @Contribute(TypeCoercer.class)
    public static void contributeTypeCoercer(Configuration<CoercionTuple> configuration) {
        configuration.add(new CoercionTuple<>(String.class, Locale.class, new StringToLocaleCoercer()));
        configuration.add(new CoercionTuple<>(Locale.class, String.class, new LocaleToStringCoercer()));
        configuration.add(new CoercionTuple<>(String.class, TimeZone.class, new StringToTimeZoneCoercer()));
        configuration.add(new CoercionTuple<>(TimeZone.class, String.class, new TimeZoneToStringCoercer()));
        configuration.add(new CoercionTuple<>(String.class, InetAddress.class, new StringToInetAddressCoercer()));
        configuration.add(new CoercionTuple<>(String.class, Path.class, new StringToPathCoercer()));
    }

    @Scope(ScopeConstants.PERTHREAD)
    public Environment buildEnvironment(PerthreadManager perthreadManager) {
        final EnvironmentImpl service = new EnvironmentImpl();
        perthreadManager.addThreadCleanupCallback(service::threadDidCleanup);
        return service;
    }

}
