package common.utils.services;

import java.text.MessageFormat;
import java.util.HashMap;

/**
 * Service for network request bodies & input generation
 */
public class GeneratorService {
    /**
     * Generate Add network request with separator included
     * @param input
     * @param separator
     * @return
     */
    public HashMap generateAddRequest(String input, String separator) {
        HashMap<String, String> addRequestMap = new HashMap<String, String>()
        {
            {
                put("input", input);
                put("separator", separator);
            }
        };

        return addRequestMap;
    }

    /**
     * Generate Add network request without separator
     * @param input
     * @return
     */
    public HashMap generateAddRequest(String input) {
        HashMap<String, String> addRequestMap = new HashMap<String, String>()
        {
            {
                put("input", input);
            }
        };

        return addRequestMap;
    }

    /**
     * Generate valid triangle input with separator
     * @param a
     * @param b
     * @param c
     * @return
     */
    public String generateValidInput(String a, String b, String c) {
        return MessageFormat.format("{0};{1};{2}", a, b, c);
    };

}
