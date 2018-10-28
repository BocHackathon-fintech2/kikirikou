package io.kikirikou.apps.backoffice.managers.decl;

import java.util.Optional;

import org.apache.tapestry5.json.JSONObject;

public interface PipelineManager {

	Optional<JSONObject> find(String id);
	JSONObject createOrUpdate(String id, JSONObject document);
	JSONObject getAll();
}
