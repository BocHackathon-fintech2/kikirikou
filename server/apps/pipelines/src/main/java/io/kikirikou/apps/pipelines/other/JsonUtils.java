package io.kikirikou.apps.pipelines.other;

import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    //TODO:Implement ability to flatten json arrays
    private static Map<String,Object> flatten(String prefix,JSONObject json,Map<String,Object> result) {
        for(String key:json.keys()) {
            String resultKey = String.format("%s%s",prefix,key);
            Object value = json.get(key);
            if(value.equals(JSONObject.NULL) || value instanceof JSONArray)
                continue;
            else if(value instanceof JSONObject)
                flatten(resultKey + ".",(JSONObject) value,result);
            else
                result.put(resultKey,value);
        }
        return result;
    }

    public static Map<String,Object> flatten(JSONObject json) {
        Map<String,Object> result = new LinkedHashMap<>();
        return flatten(StringUtils.EMPTY,json,result);
    }
}
