package io.kikirikou.modules.persistence.other;

import org.apache.tapestry5.json.JSONObject;

import java.util.function.BiConsumer;

public interface NotificationListener extends BiConsumer<String, JSONObject> {
}
