package io.kikirikou.modules.http.other;

import io.kikirikou.modules.common.other.DataSource;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.ioc.internal.util.LockSupport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class AssetDownloadDataSource extends LockSupport implements DataSource {
    private static final Function<Request.Builder, Request.Builder> DEFAULT_CUSTOMIZER = builder -> builder;
    private final OkHttpClient client;
    private final String url;
    private final Function<Request.Builder, Request.Builder> customizer;
    private AssetDownloadData data;


    public AssetDownloadDataSource(OkHttpClient client, String url, Function<Request.Builder, Request.Builder> customizer) {
        this.client = client;
        this.url = url;
        this.customizer = customizer;
    }

    public AssetDownloadDataSource(OkHttpClient client, String url) {
        this(client, url, DEFAULT_CUSTOMIZER);
    }


    @Override
    public InputStream openStream() {
        return new ByteArrayInputStream(getData().getBuffer());
    }

    @Override
    public String getName() {
        return getData().getName();
    }

    @Override
    public String getMimeType() {
        return getData().getContentType();
    }

    private AssetDownloadData getData() {
        try {
            acquireReadLock();
            if (data != null)
                return data;

            try {
                upgradeReadLockToWriteLock();
                Request request = customizer.apply(new Request.Builder().
                        url(url)).
                        build();
                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful())
                        throw new RuntimeException("Wrong response for url:" + url);
                    String filename = getFileName(response, url);
                    MediaType contentType = response.body().contentType();
                    this.data = new AssetDownloadData(
                            response.body().bytes(),
                            contentType == null ?
                                    null :
                                    contentType.toString(),
                            filename);
                    return data;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } finally {
                downgradeWriteLockToReadLock();
            }
        } finally {
            releaseReadLock();
        }
    }

    private String getFileName(Response response, String url) {
        String fileName = null;
        String pContentDisposition = response.header("Content-Disposition");
        if (StringUtils.isNotBlank(pContentDisposition)) {
            String cdl = pContentDisposition.toLowerCase();
            if (cdl.startsWith("inline") || cdl.startsWith("attachment")) {
                ParameterParser parser = new ParameterParser();
                parser.setLowerCaseNames(true);
                // Parameter parser can handle null input
                Map params = parser.parse(pContentDisposition, ';');
                if (params.containsKey("filename")) {
                    fileName = (String) params.get("filename");
                    if (fileName != null)
                        fileName = fileName.trim();
                }
            }
        }

        if (StringUtils.isBlank(fileName)) {
            int indexOfSlash = url.lastIndexOf('/');
            if (indexOfSlash > 0 && indexOfSlash != url.length() - 1)
                fileName = url.substring(indexOfSlash + 1);
        }

        if (StringUtils.isBlank(fileName))
            fileName = UUID.randomUUID().toString();

        return fileName;
    }

    private class AssetDownloadData {
        private final byte[] buffer;
        private final String contentType;
        private final String name;

        private AssetDownloadData(byte[] buffer, String contentType, String name) {
            this.buffer = buffer;
            this.contentType = contentType;
            this.name = name;
        }

        byte[] getBuffer() {
            return buffer;
        }

        String getContentType() {
            return contentType;
        }

        public String getName() {
            return name;
        }
    }
}