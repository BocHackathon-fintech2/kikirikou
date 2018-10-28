package io.kikirikou.apps.backoffice.managers.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.OneShotLock;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.apache.tapestry5.json.JSONObject;
import org.jetbrains.annotations.NotNull;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;
import org.slf4j.Logger;

import io.kikirikou.apps.backoffice.managers.decl.PipelineManager;
import io.kikirikou.apps.backoffice.other.ApplicationSymbolConstants;
import io.kikirikou.modules.common.utils.AssertUtils;

public class PipelineManagerImpl extends OneShotLock implements PipelineManager,Runnable,Serializer<JSONObject>{
	private static final String DEFAULT_MAP_NAME = "SisFeedDocumentMap";

	private final DB db;
	private final Map<String,JSONObject> cache;
	private final Logger logger;


	public PipelineManagerImpl(RegistryShutdownHub registryShutdownHub,
			Logger logger,
			@Symbol(ApplicationSymbolConstants.DB_PATH) String dbPath) {
		this.logger = logger;

		this.db = DBMaker.
				fileDB(dbPath).
				transactionEnable().
				make();

		this.cache = db.hashMap(DEFAULT_MAP_NAME).
				keySerializer(Serializer.STRING).
				valueSerializer(this).
				createOrOpen();

		registryShutdownHub.addRegistryShutdownListener(this);
	}

	@Override
	public Optional<JSONObject> find(String id) {
		AssertUtils.hasText(id,"id");
		check();
		return Optional.ofNullable(cache.get(id));
	}

	@Override
	public JSONObject getAll() {
		return new JSONObject(this.cache);
	}

	@Override
	public JSONObject createOrUpdate(String id, JSONObject document) {
		AssertUtils.hasText(id,"id");
		AssertUtils.notNull(document,"document");
		check();
		cache.put(id, document);
		db.commit();
		
		return document;
	}

	@Override
	public void run() {
		lock();
		db.close();
	}

	@Override
	public void serialize(@NotNull DataOutput2 out, @NotNull JSONObject value) throws IOException {
		out.writeUTF(value.toCompactString());
	}

	@Override
	public JSONObject deserialize(@NotNull DataInput2 input, int available) throws IOException {
		return new JSONObject(input.readUTF());
	}
}
