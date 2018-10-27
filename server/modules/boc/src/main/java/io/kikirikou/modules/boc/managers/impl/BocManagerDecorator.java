package io.kikirikou.modules.boc.managers.impl;

import io.kikirikou.modules.boc.managers.decl.BocManager;
import io.kikirikou.modules.persistence.managers.decl.QueryFactoryProvider;
import io.kikirikou.modules.persistence.other.CustomDSLContext;
import org.apache.tapestry5.json.JSONObject;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

import static io.kikirikou.modules.persistence.jooq.Tables.TRANSACTIONS;

public class BocManagerDecorator implements BocManager {
    private final QueryFactoryProvider queryFactoryProvider;

    public BocManagerDecorator(BocManager delegate,QueryFactoryProvider queryFactoryProvider) {
        this.queryFactoryProvider = queryFactoryProvider;
    }

    @Override
    public Optional<Collection<JSONObject>> getStatement(String accountId, String token, String subscriptionId, LocalDate from, LocalDate to) {
        return Optional.of(queryFactoryProvider.
                build((Function<CustomDSLContext, Collection<JSONObject>>) customDSLContext -> customDSLContext.select(TRANSACTIONS.TRANSACTION).
                from(TRANSACTIONS).
                where(TRANSACTIONS.ACCOUNT_ID.eq(accountId)).
                fetch(TRANSACTIONS.TRANSACTION)));
    }
}
