package io.kikirikou.modules.persistence.managers.decl;

import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.RecordMapperProvider;
import org.jooq.RecordType;

public interface RecordMapperProviderFilter {
    <R extends Record, E> RecordMapper<R, E> provide(RecordType<R> recordType, Class<? extends E> type, RecordMapperProvider delegate);
}
