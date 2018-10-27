package io.kikirikou.modules.persistence.other.listener;


public interface ConnectionListener {
    void commitStart();

    void commitEnd();

    void rollbackStart();

    void rollbackEnd();
}
