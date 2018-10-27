package io.kikirikou.modules.persistence.managers.decl;


import io.kikirikou.modules.persistence.other.NotificationListener;

public interface NotificationListenerManager {
    Runnable listen(String channel, NotificationListener listener);
}
