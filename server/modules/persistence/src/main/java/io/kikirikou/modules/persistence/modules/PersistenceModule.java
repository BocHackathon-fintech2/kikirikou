package io.kikirikou.modules.persistence.modules;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.kikirikou.modules.common.modules.CommonModule;
import io.kikirikou.modules.persistence.managers.decl.NotificationListenerManager;
import io.kikirikou.modules.persistence.managers.decl.QueryFactoryProvider;
import io.kikirikou.modules.persistence.managers.decl.RecordMapperProviderFilter;
import io.kikirikou.modules.persistence.managers.impl.NotificationListenerManagerImpl;
import io.kikirikou.modules.persistence.managers.impl.QueryFactoryProviderImpl;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.services.PipelineBuilder;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.jooq.RecordMapperProvider;
import org.simpleflatmapper.jooq.SfmRecordMapperProvider;
import org.simpleflatmapper.jooq.SfmRecordMapperProviderFactory;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

@ImportModule(CommonModule.class)
public class PersistenceModule {
    public static void bind(ServiceBinder binder) {
        binder.bind(QueryFactoryProvider.class, QueryFactoryProviderImpl.class);
        binder.bind(NotificationListenerManager.class, NotificationListenerManagerImpl.class);
    }

    public static RecordMapperProvider buildRecordMapperProvider(PipelineBuilder builder,
                                                                 List<RecordMapperProviderFilter> configuration,
                                                                 Logger logger) {
        SfmRecordMapperProvider terminator = SfmRecordMapperProviderFactory.
                newInstance().
                useAsm(true).
                failOnAsm(true).
                ignorePropertyNotFound().
                newProvider();
        return builder.build(logger, RecordMapperProvider.class, RecordMapperProviderFilter.class,
                configuration, terminator);
    }

    public DataSource buildDataSource(RegistryShutdownHub registryShutdownHub) {
        Properties settings = new Properties();
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("conf/db.properties")) {
            settings.load(is);
        } catch (IOException ignored) {}

        settings.put("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        HikariDataSource hikariDataSource = new HikariDataSource(new HikariConfig(settings));
        registryShutdownHub.addRegistryShutdownListener((Runnable) hikariDataSource::close);
        return hikariDataSource;
    }

}
