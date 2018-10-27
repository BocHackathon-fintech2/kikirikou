package io.kikirikou.apps.pipelines.managers.impl;

import java.util.Optional;
import java.util.Set;

public interface SmsManager {
	Optional<Set<String>> send(String from, String[] addresses, String body);
}