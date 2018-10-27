package io.kikirikou.modules.boc.managers.decl;

import org.apache.tapestry5.json.JSONObject;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface BocManager {
    Optional<Collection<JSONObject>> getStatement(String accountId, String token,
                                                  String subscriptionId,
                                                  LocalDate from,
                                                  LocalDate to);
}
