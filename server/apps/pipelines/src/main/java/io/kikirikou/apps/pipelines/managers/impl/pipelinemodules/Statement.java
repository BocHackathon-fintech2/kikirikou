package io.kikirikou.apps.pipelines.managers.impl.pipelinemodules;

import io.kikirikou.apps.pipelines.other.PipelineProcessor;
import io.kikirikou.modules.boc.managers.decl.BocManager;
import org.apache.tapestry5.json.JSONObject;
import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Stream;

public class Statement implements PipelineProcessor {
    private final BocManager bocManager;

    public Statement(BocManager bocManager) {
        this.bocManager = bocManager;
    }

    @Override
    public Stream<JSONObject> process(Stream<JSONObject> stream, JSONObject config) {
        LocalDate from = LocalDate.parse(config.getString("from"));
        LocalDate to = LocalDate.parse(config.getString("to"));
        String token = config.getString("token");        
        String account = config.getString("account");
        String subscriptionId = config.getString("subscriptionId");

        Stream<JSONObject> transactionStream = bocManager.getStatement(account, token, subscriptionId, from, to).
                map(Collection::stream).
                orElseThrow(IllegalAccessError::new);

        return Stream.concat(
                stream.filter(jsonObject -> false),
                transactionStream);
    }

    public static void main(String[] args) {
        System.out.println("Hello:" + LocalDate.now().toString());
    }
}
