package io.kikirikou.modules.resteasy.other;

import io.kikirikou.modules.common.utils.AssertUtils;
import io.kikirikou.modules.jetty.managers.decl.BaseURLSource;
import io.kikirikou.modules.jetty.other.Link;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.ioc.services.TypeCoercer;

public class RestResourceLinkBuilder {
    private final BaseURLSource baseURLSource;
    private final String mappingPrefix;
    private final TypeCoercer typeCoercer;
    private final Class resource;

    private String method;
    private Object[] params;

    public RestResourceLinkBuilder(BaseURLSource baseURLSource,
                                   String mappingPrefix,
                                   TypeCoercer typeCoercer,
                                   Class resource) {
        this.baseURLSource = baseURLSource;
        this.mappingPrefix = mappingPrefix;
        this.typeCoercer = typeCoercer;
        this.resource = resource;
    }

    public RestResourceLinkBuilder withMethod(String method) {
        this.method = method;
        return this;
    }

    public RestResourceLinkBuilder withParams(Object... params) {
        AssertUtils.assertThat(params == null || params.length % 2 == 0, "Params not mod 2");
        this.params = params;
        return this;
    }

    public Link build() {
        String baseUrl = baseURLSource.getBaseURL();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl).newBuilder();
        if (StringUtils.isNotBlank(mappingPrefix))
            urlBuilder = urlBuilder.addPathSegment(mappingPrefix.substring(1));
        if (resource != null) {
            String uri = javax.ws.rs.core.Link.fromResource(resource).
                    build().
                    getUri().
                    toString();


            String paths = uri + (StringUtils.isBlank(method) ? StringUtils.EMPTY : method);
            for (String path : paths.split("/"))
                urlBuilder.addPathSegment(path);

            if (params != null) {
                int i = 0;
                while (i < params.length)
                    urlBuilder = urlBuilder.addQueryParameter(
                            typeCoercer.coerce(params[i++], String.class),
                            typeCoercer.coerce(params[i++], String.class));
            }

        }

        return new Link(baseUrl, urlBuilder.build());

    }
}
