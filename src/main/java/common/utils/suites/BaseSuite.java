package common.utils.suites;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseSuite {

    /**
     * Extract response body
     *
     * @param response
     * @return
     */
    protected HashMap extractBodyMap(Response response) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return mapper.readValue(response.body().string(), HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Extract response body
     *
     * @param response
     * @return
     */
    protected List<Map<String, String>> extractBodyList(Response response) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return (List<Map<String, String>>) mapper.readValue(response.body().string(), List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
