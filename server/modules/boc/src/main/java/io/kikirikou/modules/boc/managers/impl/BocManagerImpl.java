package io.kikirikou.modules.boc.managers.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.json.JSONArray;
import org.slf4j.Logger;

import io.kikirikou.modules.boc.managers.decl.BocManager;
import io.kikirikou.modules.boc.other.BocSymbolConstants;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BocManagerImpl implements BocManager {

	private final String clientId;
	private final String clientSecret;
	private final String baseUrl;
	private final OkHttpClient httpClient;
	private final DateTimeFormatter df;
	private final Logger log;

	public BocManagerImpl(
			@Symbol(BocSymbolConstants.CLIENT_ID) String clientId,
			@Symbol(BocSymbolConstants.CLIENT_SECRET) String clientSecret,
			@Symbol(BocSymbolConstants.BASE_URL) String baseUrl,
			OkHttpClient httpClient,
			Logger log) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.baseUrl = baseUrl;
		this.httpClient = httpClient;
		this.log = log;

		df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	}

	public Optional<Collection<JSONObject>> getStatement(String accountId, String token,
														 String subscriptionId,
														 LocalDate from,
														 LocalDate to) {

		String url = this.baseUrl + "/v1/accounts/" + accountId + "/statement";

		HttpUrl httpUrl = HttpUrl.parse(url).newBuilder()
				.addQueryParameter("client_id", this.clientId)
				.addQueryParameter("client_secret", this.clientSecret)
				.addQueryParameter("startDate", from.format(df))
				.addQueryParameter("endDate", to.format(df))
				.addQueryParameter("maxCount", "0").build();

		Request req = new Request.Builder()
				.url(httpUrl)
				.addHeader("Authorization", "Bearer " + token)
				.addHeader("subscriptionId", subscriptionId)
				.addHeader("originUserId", "ORIGIN_USER_ID")
				.addHeader("tppId", "singpaymentdata")
				.addHeader("journeyId", "abc")
				.addHeader("timestamp", Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)))
				.addHeader("Accept", "application/json")
				.get()
				.build();

		try (Response res =  this.httpClient.newCall(req).execute()) {
			if (res.isSuccessful()) {
				return Optional.of(new JSONArray(res.body().string()).
						getJSONObject(0).
						getJSONArray("transaction").
						toList().
						stream().
						map(o -> (JSONObject) o).collect(Collectors.toList()));
			} else {
				log.error("Statement request not successful. Code: {}", res.code());
				return Optional.empty();
			}
		} catch (IOException e) {
			log.error("Statement request not successful.", e);
			return Optional.empty();
		}

	} 
}
