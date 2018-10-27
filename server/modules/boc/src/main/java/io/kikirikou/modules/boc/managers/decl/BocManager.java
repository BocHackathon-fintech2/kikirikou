package io.kikirikou.modules.boc.managers.decl;

import org.apache.tapestry5.json.JSONArray;

import java.time.LocalDate;
import java.util.Optional;

public interface BocManager {
    Optional<JSONArray> getStatement(String accountId, String token,
                                     String subscriptionId,
                                     LocalDate from,
                                     LocalDate to);
}
