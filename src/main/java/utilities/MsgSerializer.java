/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author leonardo
 */
public class MsgSerializer {
    public static String serialize(String msg) throws JsonProcessingException {
        Map<String, Object> params = new HashMap<>();
        params.put("message", msg);
        return new ObjectMapper().writeValueAsString(params);
    }
    public static String serialize(String key, String value) throws JsonProcessingException {
        Map<String, Object> params = new HashMap<>();
        params.put(key, value);
        return new ObjectMapper().writeValueAsString(params);
    }
}
