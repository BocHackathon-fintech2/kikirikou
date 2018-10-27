package io.kikirikou.modules.persistence.other.listener;

import io.kikirikou.modules.persistence.other.listener.ConnectionListener;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import java.util.Collection;

public class ConnectionListeners implements ConnectionListener {
    private final Collection<ConnectionListener> listeners;

    public ConnectionListeners() {
        this.listeners = CollectionFactory.newThreadSafeList();
    }

    @Override
    public void commitStart() {
        for (ConnectionListener listener : listeners)
            listener.commitStart();
    }

    @Override
    public void commitEnd() {
        for (ConnectionListener listener : listeners)
            listener.commitEnd();
    }

    @Override
    public void rollbackStart() {
        for (ConnectionListener listener : listeners)
            listener.rollbackStart();
    }

    @Override
    public void rollbackEnd() {
        for (ConnectionListener listener : listeners)
            listener.rollbackEnd();
    }

    public void addListener(ConnectionListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ConnectionListener listener) {
        listeners.remove(listener);
    }
}
