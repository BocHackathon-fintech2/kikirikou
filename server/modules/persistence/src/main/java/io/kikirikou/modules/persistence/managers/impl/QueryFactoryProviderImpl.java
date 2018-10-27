package io.kikirikou.modules.persistence.managers.impl;

import io.kikirikou.modules.persistence.managers.decl.QueryFactoryProvider;
import io.kikirikou.modules.persistence.other.CustomDSLContext;
import io.kikirikou.modules.persistence.other.listener.ConnectionListeners;
import org.apache.tapestry5.ioc.services.PerThreadValue;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.RecordMapperProvider;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.ThreadLocalTransactionProvider;

import javax.sql.DataSource;
import java.util.function.Function;

public class QueryFactoryProviderImpl implements QueryFactoryProvider {
    private final DataSource dataSource;
    private final PerThreadValue<CustomDSLContext> configuration;
    private final RecordMapperProvider recordMapperProvider;

    public QueryFactoryProviderImpl(DataSource dataSource,
                                    PerthreadManager perthreadManager,
                                    RecordMapperProvider recordMapperProvider) {
        this.dataSource = dataSource;
        this.configuration = perthreadManager.createValue();
        this.recordMapperProvider = recordMapperProvider;
    }

    @Override
    public <R> R build(Function<CustomDSLContext, R> function) {
        if (configuration.get() == null) {
            ConnectionListeners listeners = new ConnectionListeners();
            ConnectionProvider connectionProvider = new DataSourceConnectionProvider(dataSource);
            Configuration local = new DefaultConfiguration().
                    set(connectionProvider).
                    set(recordMapperProvider).
                    set(new ThreadLocalTransactionProvider(connectionProvider,true)).
                    set(SQLDialect.POSTGRES);
            local.data(ConnectionListeners.class, listeners);
            return configuration.set(new CustomDSLContext(local)).transactionResult(() -> function.andThen(r -> {
                configuration.set(null);
                return r;
            }).apply(configuration.get()));
        }else
            return function.apply(configuration.get());

    }
}
