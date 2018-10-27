package io.kikirikou.apps.pipelines.managers.impl.pipelinemodules;

import java.util.stream.Stream;

import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONObject;

import io.kikirikou.apps.pipelines.managers.impl.SmsManager;
import io.kikirikou.apps.pipelines.other.ApplicationSymbolConstants;
import io.kikirikou.apps.pipelines.other.JsonUtils;
import io.kikirikou.apps.pipelines.other.PipelineProcessor;

public class Sms implements PipelineProcessor {
	private final SmsManager smsManager;
	private final String from;
	private final TypeCoercer typeCoercer;

	public Sms(SmsManager smsManager, @Symbol(ApplicationSymbolConstants.SMS_FROM) String from,
			TypeCoercer typeCoercer) {
		this.smsManager = smsManager;
		this.from = from;
		this.typeCoercer = typeCoercer;
	}

	@Override
	public Stream<JSONObject> process(Stream<JSONObject> stream, JSONObject config) {
		String[] phones = config.getJSONArray("phones").toList().stream().map(o -> (String)o).toArray(String[]::new);
		String[] columns = config.getJSONArray("columns").toList().stream().map(o -> (String)o).toArray(String[]::new);
		String text = config.getString("text");

		return stream.peek((arg0)-> {
			String body = text;
			for (String column: columns) {
				String val = typeCoercer.coerce(JsonUtils.flatten(arg0).get(column), String.class);

				body = body.replaceAll("\\{" + column + "\\}", val);
				
			}
			this.smsManager.send(from, phones, body);
		});
	}
}
