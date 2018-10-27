package io.kikirikou.modules.bootstrap;


import io.kikirikou.modules.bootstrap.managers.decl.AppInitializer;
import io.kikirikou.modules.bootstrap.managers.impl.AppInitializerImpl;
import org.apache.tapestry5.ioc.internal.services.MapSymbolProvider;
import org.apache.tapestry5.ioc.services.SymbolProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Bootstrap {
    private static final Object lock = new Object();

    private static Map<String, String> loadFile(String... files) {
        Map<String, String> map = new HashMap<>();
        for (String file : files) {
            try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(file)) {
                if (stream == null)
                    continue;
                Properties settings = new Properties();
                settings.load(stream);
                for (String key : settings.stringPropertyNames())
                    map.put(key, settings.getProperty(key));
            } catch (IOException ignored) {
            }
        }
        return map;
    }

    public static void main(String[] args) throws Exception {
        SymbolProvider settingsSymbolProvider = new MapSymbolProvider(loadFile("conf/bootstrap.properties", "conf/app.properties"));
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
        AppInitializer initializer = new AppInitializerImpl(settingsSymbolProvider);
        initializer.start();
        synchronized (lock) {
            lock.wait();
        }
        initializer.stop();
    }


    private static class ShutdownHook extends Thread {
        @Override
        public void run() {
            synchronized (lock) {
                try {
                    lock.notify();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
