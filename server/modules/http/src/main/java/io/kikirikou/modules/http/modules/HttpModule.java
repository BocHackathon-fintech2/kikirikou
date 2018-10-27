package io.kikirikou.modules.http.modules;


import io.kikirikou.modules.common.managers.decl.StringToDataSourceManager;
import io.kikirikou.modules.common.managers.decl.StringToDataSourceProcessor;
import io.kikirikou.modules.common.modules.CommonModule;
import io.kikirikou.modules.http.managers.impl.HttpProcessor;
import io.kikirikou.modules.http.other.HttpSymbolConstants;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.tapestry5.ioc.LoggerSource;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.FactoryDefaults;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@ImportModule(value = {CommonModule.class})
public class HttpModule {
    @Contribute(SymbolProvider.class)
    @FactoryDefaults
    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
        configuration.add(HttpSymbolConstants.MAX_CONNECTIONS, 100);
        configuration.add(HttpSymbolConstants.CONNECT_TIMEOUT_IN_MS, 3000);
        configuration.add(HttpSymbolConstants.READ_TIMEOUT_IN_MS, 0);
        configuration.add(HttpSymbolConstants.WRITE_TIMEOUT_IN_MS, 0);
        configuration.add(HttpSymbolConstants.CONNECTION_KEEP_ALIVE_IN_MS, 5 * 60 * 1000);
        configuration.add(HttpSymbolConstants.CACHE_LOCATION, "${java.io.tmpdir}/cache/http");
        configuration.add(HttpSymbolConstants.CACHE_SIZE_IN_BYTES, 10 * 10 * 1024 * 1024);
    }


    public static OkHttpClient buildOkHttpClient(RegistryShutdownHub hub,
                                                 ExecutorService executorService,
                                                 LoggerSource loggerSource,
                                                 @Symbol(HttpSymbolConstants.CONNECT_TIMEOUT_IN_MS) int connectTimeout,
                                                 @Symbol(HttpSymbolConstants.READ_TIMEOUT_IN_MS) int readTimeout,
                                                 @Symbol(HttpSymbolConstants.WRITE_TIMEOUT_IN_MS) int writeTimeout,
                                                 @Symbol(HttpSymbolConstants.MAX_CONNECTIONS) int maxConnections,
                                                 @Symbol(HttpSymbolConstants.CONNECTION_KEEP_ALIVE_IN_MS) int keepAlive,
                                                 @Symbol(HttpSymbolConstants.CACHE_LOCATION) String cacheLocation,
                                                 @Symbol(HttpSymbolConstants.CACHE_SIZE_IN_BYTES) long cacheSize) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder().
                connectTimeout(connectTimeout, TimeUnit.MILLISECONDS).
                readTimeout(readTimeout, TimeUnit.MILLISECONDS).
                writeTimeout(writeTimeout, TimeUnit.MILLISECONDS).
                dispatcher(new Dispatcher(executorService)).
                connectionPool(new ConnectionPool(maxConnections, keepAlive, TimeUnit.MILLISECONDS)).
                cache(new Cache(new File(cacheLocation), cacheSize));

        Logger logger = loggerSource.getLogger(OkHttpClient.class);
        if(loggerSource.getLogger(OkHttpClient.class).isDebugEnabled()) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(logger::debug);
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        OkHttpClient client = builder.build();
        hub.addRegistryShutdownListener((Runnable) () -> {
            client.dispatcher().executorService().shutdown();
            client.connectionPool().evictAll();
            try {
                client.cache().close();
            } catch (IOException ignored) {}
        });

        return client;
    }

    @Contribute(StringToDataSourceManager.class)
    public static void contributeStringToDataSourceManager(MappedConfiguration<String, StringToDataSourceProcessor> configuration,
                                                           @Autobuild HttpProcessor httpProcessor) {
        configuration.add("http", httpProcessor);
        configuration.add("https", httpProcessor);
    }
}
