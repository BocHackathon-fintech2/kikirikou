package io.kikirkou.modules.resteasy.rest;

import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONCollection;
import org.jboss.resteasy.plugins.providers.ProviderHelper;
import org.jboss.resteasy.util.NoContent;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JSONCollectionProvider implements MessageBodyReader<JSONCollection>, MessageBodyWriter<JSONCollection> {
    private final boolean productionMode;
    private final TypeCoercer typeCoercer;

    public JSONCollectionProvider(TypeCoercer typeCoercer, boolean productionMode) {
        this.typeCoercer = typeCoercer;
        this.productionMode = productionMode;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(JSONCollection jsonCollection, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(JSONCollection jsonCollection, Class<?> type, Type genericType, Annotation[] annotations,
                        MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws WebApplicationException {
        try (PrintStream ps = new PrintStream(entityStream)) {
            ps.print(jsonCollection.toString(productionMode));
        }
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return JSONCollection.class.isAssignableFrom(type);
    }

    @Override
    public JSONCollection readFrom(Class<JSONCollection> type, Type genericType, Annotation[] annotations,
                                   MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        InputStream delegate = NoContent.noContentCheck(httpHeaders, entityStream);
        String value = ProviderHelper.readString(delegate, mediaType);
        return typeCoercer.coerce(value, type);
    }
}