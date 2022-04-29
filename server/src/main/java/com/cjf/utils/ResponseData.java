package com.cjf.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

// 封装一下响应体
@Data
public class ResponseData {
    private  String msg;
    private  int code;
    private  Map<String, Object> data = new HashMap<String, Object>();

    public ResponseData putDataValue(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public ResponseData(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
