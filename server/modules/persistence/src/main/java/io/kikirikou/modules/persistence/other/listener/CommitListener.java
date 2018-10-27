package io.kikirikou.modules.persistence.other.listener;

import io.kikirikou.modules.persistence.other.listener.ConnectionListener;

public abstract class CommitListener implements ConnectionListener {
    @Override
    public void commitStart() {
    }

    @Override
    public void rollbackStart() {
    }

    @Override
    public void rollbackEnd() {
    }
}
