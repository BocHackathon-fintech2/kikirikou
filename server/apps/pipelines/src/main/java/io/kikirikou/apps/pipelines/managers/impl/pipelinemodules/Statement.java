package io.kikirikou.apps.pipelines.managers.impl.pipelinemodules;

import io.kikirikou.apps.pipelines.other.PipelineProcessor;
import org.apache.tapestry5.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.stream.Stream;

public class Statement implements PipelineProcessor {
    @Override
    public Stream<JSONObject> process(Stream<JSONObject> stream, JSONObject config) {
        LocalDate.parse(config.getString("from"));
        LocalDate.parse(config.getString("to"));
        String token = config.getString("token");



        //Do you get request

        //Return result
        return Stream.concat(
                stream.filter(jsonObject -> false),
                Stream.of(new JSONObject("id", "e", "value1", 1, "value2", 4),
                        new JSONObject("id", "f", "value1", 2.0, "value2", 5),
                        new JSONObject("id", "g", "value1", new BigDecimal(4.6).setScale(2, RoundingMode.HALF_EVEN), "value2", 6)));
    }
}
