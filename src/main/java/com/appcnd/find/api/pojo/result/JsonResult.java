package com.appcnd.find.api.pojo.result;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.http.HttpStatus;

import java.util.HashMap;

/**
 * Created by nihao on 17/12/15.
 */
public class JsonResult<K,V> extends HashMap<K,V> {

    public String json(){
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.SkipTransientField,
                SerializerFeature.WriteBigDecimalAsPlain);
    }

    public JsonResult<K,V> pull(K key, V value){
        put(key, value);
        return this;
    }

    public static JsonResult success(){
        return success("success");
    }

    public static JsonResult fail(){
        return fail("fail");
    }

    public static JsonResult success(String message){
        return new JsonResult().pull("code", HttpStatus.SC_OK).pull("message", message);
    }

    public static JsonResult fail(String message){
        return new JsonResult().pull("code", HttpStatus.SC_INTERNAL_SERVER_ERROR).pull("message", message);
    }

    public JsonResult() {

    }
}
