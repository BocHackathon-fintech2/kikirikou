package io.kikirikou.apps.pipelines.managers.impl.pipelinemodules;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONObject;
import org.slf4j.Logger;

import io.kikirikou.apps.pipelines.other.PipelineProcessor;
import io.kikirikou.modules.boc.enums.TransactionType;
import io.kikirikou.modules.boc.managers.decl.BocManager;

public class Payment implements PipelineProcessor {
    private final BocManager bocManager;
	private final TypeCoercer typeCoercer;

    public Payment(Logger logger, BocManager bocManager, TypeCoercer typeCoercer) {
        this.bocManager = bocManager;
        this.typeCoercer = typeCoercer;
    }

    @Override
    public Stream<JSONObject> process(Stream<JSONObject> stream, JSONObject config) {
    	String fromAccountId = config.getString("from");
    	String toAccountId = config.getString("to");
    	BigDecimal amount = typeCoercer.coerce(config.get("amount"), BigDecimal.class);
    	TransactionType transactionType = typeCoercer.coerce(config.get("type"), TransactionType.class);

    	return stream.peek(jsonObject -> bocManager.createTransfer(fromAccountId, toAccountId, transactionType, amount));
    }
}
