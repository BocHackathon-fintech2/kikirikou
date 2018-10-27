package io.kikirikou.apps.pipelines.managers.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.json.JSONObject;
import org.slf4j.Logger;

import io.kikirikou.apps.pipelines.other.ApplicationSymbolConstants;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SmsManagerImpl implements SmsManager {
    //private static final String DEFAULT_REGION = "CY";
    private static final String ENDPOINT = "https://api.plivo.com/v1/Account";
    private static final int MAXIMUM_FROM_LENGTH = 11;

    private final OkHttpClient httpClient;
    private final Logger logger;
    private final String authId;
    private final String authToken;
    private final URL endPoint;

    public SmsManagerImpl(OkHttpClient httpClient,
                          Logger logger,
                          @Symbol(ApplicationSymbolConstants.SMS_AUTH_ID) String authId,
                          @Symbol(ApplicationSymbolConstants.SMS_AUTH_TOKEN) String authToken) throws MalformedURLException {
        this.httpClient = httpClient;
        this.logger = logger;
        this.authId = authId;
        this.authToken = authToken;
        this.endPoint = new URL(ENDPOINT);
    }

    @Override
    public Optional<Set<String>> send(String from, String[] addresses, String body) {
        Map<String, String> fixedAddresses = Arrays.stream(addresses).
                map(s -> new ImmutablePair<>(s, fixAddress(s))).
                filter(pair -> StringUtils.isNotBlank(pair.getRight())).
                collect(Collectors.toMap(ImmutablePair::getLeft, ImmutablePair::getRight,(p1, p2) -> p1));

        if(fixedAddresses.isEmpty()) {
            logger.error("After filtering phones there were no valid phone numbers left");
            return Optional.empty();
        }

        JSONObject payload = new JSONObject(
                "src",from.substring(0,Math.min(MAXIMUM_FROM_LENGTH,from.length())),
                "dst",StringUtils.join(fixedAddresses.values().stream().distinct().toArray(String[]::new),'<'),
                "text",body
                //"url",callback.toString()
        );

        HttpUrl url = HttpUrl.get(endPoint).
                newBuilder().
                addPathSegments(authId).
                addPathSegment("Message/"). //NOTE:Slash has to be appended or the call will not work (PLIVO)
                build();

        Request request = new Request.Builder()
                .addHeader("Authorization", Credentials.basic(authId, authToken))
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json"),
                        payload.toCompactString()))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.error("Got wrong status code {} from sms notification {}",
                        response.code(),
                        url.toString());
                return Optional.empty();
            }

            return Optional.of(fixedAddresses.keySet());
        } catch (IOException ex) {
            logger.error("Error sending sms",ex);
            return Optional.empty();
        }
    }

    private String fixAddress(String address) {
    	if (!address.startsWith("+")) {
    		return "+357"+address;
    	}
    	
    	return address;
    }
}