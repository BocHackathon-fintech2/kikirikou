package io.kikirikou.modules.jetty.other;

import io.kikirikou.modules.jetty.managers.decl.BaseURLSource;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.ioc.util.LocalizedNameGenerator;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

public class AssetLinkBuilder {
    private final BaseURLSource baseURLSource;
    private final ResourceCollection resourceCollection;
    private final String mappingPrefix;
    private final String path;

    private Locale locale;

    public AssetLinkBuilder(BaseURLSource baseURLSource,
                            ResourceCollection resourceCollection,
                            String mappingPrefix,
                            String path) {
        this.baseURLSource = baseURLSource;
        this.resourceCollection = resourceCollection;
        this.mappingPrefix = mappingPrefix;
        this.path = path;
    }

    public AssetLinkBuilder withLocale(Locale locale) {
        this.locale = locale;
        return this;
    }

    public Optional<Link> build() {
        String baseUrl = baseURLSource.getBaseURL();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl).newBuilder();
        if (StringUtils.isNotBlank(mappingPrefix))
            urlBuilder.addPathSegment(mappingPrefix.substring(1));

        try {
            String finalPath = path;
            if (locale != null) {
                for (String path : new LocalizedNameGenerator(finalPath, locale)) {
                    Resource resource = resourceCollection.addPath(path);
                    if (resource.exists())
                        finalPath = path;
                }
            }

            if (!resourceCollection.addPath(finalPath).exists())
                return Optional.empty();

            for (String path : finalPath.split("/"))
                urlBuilder.addPathSegment(path);

            return Optional.of(new Link(baseUrl, urlBuilder.build()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
