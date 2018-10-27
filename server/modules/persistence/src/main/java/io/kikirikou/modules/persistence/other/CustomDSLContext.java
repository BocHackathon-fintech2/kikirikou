package io.kikirikou.modules.persistence.other;

import io.kikirikou.modules.persistence.other.listener.ConnectionListener;
import io.kikirikou.modules.persistence.other.listener.ConnectionListeners;
import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DefaultDSLContext;

import javax.sql.DataSource;
import java.sql.Connection;

public class CustomDSLContext extends DefaultDSLContext {
    public CustomDSLContext(SQLDialect dialect) {
        super(dialect);
    }

    public CustomDSLContext(SQLDialect dialect, Settings settings) {
        super(dialect, settings);
    }

    public CustomDSLContext(Connection connection, SQLDialect dialect) {
        super(connection, dialect);
    }

    public CustomDSLContext(Connection connection, SQLDialect dialect, Settings settings) {
        super(connection, dialect, settings);
    }

    public CustomDSLContext(DataSource datasource, SQLDialect dialect) {
        super(datasource, dialect);
    }

    public CustomDSLContext(DataSource datasource, SQLDialect dialect, Settings settings) {
        super(datasource, dialect, settings);
    }

    public CustomDSLContext(ConnectionProvider connectionProvider, SQLDialect dialect) {
        super(connectionProvider, dialect);
    }

    public CustomDSLContext(ConnectionProvider connectionProvider, SQLDialect dialect, Settings settings) {
        super(connectionProvider, dialect, settings);
    }

    public CustomDSLContext(Configuration configuration) {
        super(configuration);
    }

    public CustomDSLContext addListener(ConnectionListener listener) {
        ((ConnectionListeners) configuration().data(ConnectionListeners.class)).addListener(listener);
        return this;
    }

    public CustomDSLContext removeListener(ConnectionListener listener) {
        ((ConnectionListeners) configuration().data(ConnectionListeners.class)).removeListener(listener);
        return this;
    }
}
