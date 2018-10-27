package io.kikirikou.modules.bootstrap.managers.impl;

import io.kikirikou.modules.bootstrap.managers.decl.AppInitializer;
import io.kikirikou.modules.bootstrap.modules.BootstrapDevelopmentModule;
import io.kikirikou.modules.bootstrap.modules.BootstrapModule;
import io.kikirikou.modules.bootstrap.other.SyntheticModuleDef;
import io.kikirikou.modules.bootstrap.other.SyntheticSymbolSourceContributionDef;
import io.kikirikou.modules.bootstrap.other.constants.BootstrapConstants;
import io.kikirikou.modules.bootstrap.other.constants.BootstrapSymbolConstants;
import io.kikirikou.modules.common.modules.CommonModule;
import io.kikirikou.modules.common.other.CommonSymbolConstants;
import io.kikirikou.modules.common.other.DelegatingSymbolProvider;
import io.kikirikou.modules.common.other.SingleKeySymbolProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.ioc.IOCUtilities;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.apache.tapestry5.ioc.internal.services.SystemPropertiesSymbolProvider;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.ioc.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Formatter;
import java.util.List;

public class AppInitializerImpl implements AppInitializer {
    private static final String DEVELOPMENT_EXECUTION_MODE = "development";
    private static final Logger logger = LoggerFactory.getLogger(AppInitializerImpl.class);
    private static final Logger appLogger = LoggerFactory.getLogger("Application");
    private final Registry registry;
    private final long startTime;
    private final long registryCreatedTime;


    public AppInitializerImpl(SymbolProvider applicationSymbolProvider) {
        startTime = System.currentTimeMillis();
        List<SymbolProvider> providers = CollectionFactory.newList(new SystemPropertiesSymbolProvider());
        if (applicationSymbolProvider != null)
            providers.add(applicationSymbolProvider);
        providers.add(new SingleKeySymbolProvider(BootstrapConstants.EXECUTION_MODE, "production"));
        SymbolProvider combinedProvider = new DelegatingSymbolProvider(providers.toArray(new SymbolProvider[0]));

        String executionModes = combinedProvider.valueForSymbol(BootstrapConstants.EXECUTION_MODE);
        String appPackage = combinedProvider.valueForSymbol(BootstrapConstants.APP_PACKAGE);
        String appName = combinedProvider.valueForSymbol(BootstrapConstants.APP_NAME);
        RegistryBuilder builder = new RegistryBuilder();
        IOCUtilities.addDefaultModules(builder);
        builder.add(CommonModule.class);
        builder.add(BootstrapModule.class);
        if (executionModes.toLowerCase().contains(DEVELOPMENT_EXECUTION_MODE))
            builder.add(BootstrapDevelopmentModule.class);

        String className = String.format("%s.modules.%sModule", appPackage, InternalUtils.capitalize(appName));
        try {
            Class moduleClass = Thread.currentThread().getContextClassLoader().loadClass(className);
            builder.add(moduleClass);
        } catch (ClassNotFoundException ex) {
            logger.warn("Application Module class {} not found", className);
        }
        builder.add(new SyntheticModuleDef(
                new SyntheticSymbolSourceContributionDef("Default",
                        combinedProvider, "before:ApplicationDefaults", "after:EnvironmentVariables")));
        for (String mode : StringUtils.split(executionModes, ",")) {
            String key = String.format("io.kikirikou.modules.bootstrap.%s-modules", mode);
            String moduleList = combinedProvider.valueForSymbol(key);
            if (StringUtils.isBlank(moduleList)) {
                continue;
            }
            for (String moduleClassName : StringUtils.split(moduleList, ",")) {
                builder.add(moduleClassName);
            }
        }
        registryCreatedTime = System.currentTimeMillis();
        registry = builder.build();
    }

    @Override
    public void start() {
        registry.performRegistryStartup();
        if (logger.isInfoEnabled()) {
            long finishTime = System.currentTimeMillis();

            SymbolSource source = registry.getService("SymbolSource", SymbolSource.class);

            StringBuilder buffer = new StringBuilder("Startup status:\n\nServices:\n\n");
            Formatter f = new Formatter(buffer);


            int unrealized = 0;

            ServiceActivityScoreboard scoreboard = registry.getService(ServiceActivityScoreboard.class);

            List<ServiceActivity> serviceActivity = scoreboard.getServiceActivity();

            int longest = 0;

            // One pass to find the longest name, and to count the unrealized services.

            for (ServiceActivity activity : serviceActivity) {
                Status status = activity.getStatus();

                longest = Math.max(longest, activity.getServiceId().length());

                if (status == Status.DEFINED || status == Status.VIRTUAL)
                    unrealized++;
            }

            String formatString = "%" + longest + "s: %s\n";

            // A second pass to output all the services

            for (ServiceActivity activity : serviceActivity) {
                f.format(formatString, activity.getServiceId(), activity.getStatus().name());
            }

            f.format("\n%4.2f%% unrealized services (%d/%d).Startup time: %,d ms to build IoC Registry, %,d ms overall",
                    100. * unrealized / serviceActivity.size(), unrealized,
                    serviceActivity.size(),
                    registryCreatedTime - startTime,
                    finishTime - startTime);

            boolean productionMode = Boolean.parseBoolean(source.valueForSymbol(CommonSymbolConstants.PRODUCTION_MODE));


            buffer.append("\n\n");
            buffer.append("\n" +
                    "88  dP 88 88  dP 88 88\"\"Yb 88 88  dP  dP\"Yb  88   88 \n" +
                    "88odP  88 88odP  88 88__dP 88 88odP  dP   Yb 88   88 \n" +
                    "88\"Yb  88 88\"Yb  88 88\"Yb  88 88\"Yb  Yb   dP Y8   8P \n" +
                    "88  Yb 88 88  Yb 88 88  Yb 88 88  Yb  YbodP  `YbodP' \n");
            buffer.append("\n\n");
            f.format("Application: %s (version: %s%s)\n\n",
                    source.valueForSymbol(BootstrapConstants.APP_NAME),
                    source.valueForSymbol(BootstrapSymbolConstants.APPLICATION_VERSION),
                    productionMode ? "" : " - development mode");

            // log multi-line string with OS-specific line endings (TAP5-2294)
            logger.info(buffer.toString().replaceAll("\\n", System.getProperty("line.separator")));
        }
        registry.cleanupThread();
    }

    @Override
    public void stop() {
        registry.cleanupThread();
        registry.shutdown();
    }
}
