package io.kikirikou.modules.common.other;

import java.io.IOException;
import java.io.InputStream;

public interface DataSource {
    String getName();

    String getMimeType() throws IOException;

    InputStream openStream() throws IOException;
}
