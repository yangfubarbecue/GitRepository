package com.abc.p2p.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author yang
 * @Date 2020/10/15 20:10
 * @Description :
 */
public class Result {

    public static Map<String,String> SUCCESS(String message) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "1");
        map.put("message", message);
        map.put("success", "SUCCESS");
        return map;
    }

    public static Map<String,String> ERROR(String message) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "-1");
        map.put("message", message);
        map.put("error", "ERROR");
        return map;
    }
}
