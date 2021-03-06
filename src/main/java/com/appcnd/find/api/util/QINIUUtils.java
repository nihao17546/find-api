package com.appcnd.find.api.util;

import com.alibaba.fastjson.JSON;
import com.appcnd.find.api.conf.SecretConfig;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

/**
 * Created by nihao on 17/9/5.
 */
@Deprecated
@Component
public class QINIUUtils {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SecretConfig secretConfig;

    private final String lookBucket = "mydata";
    public final static String URL_PREFIX = "${mydata}";

    public Result upload(File file, String key, String lookBucket){
        Result result = new Result();
        Configuration configuration = new Configuration();
        UploadManager uploadManager = new UploadManager(configuration);
        Auth auth = Auth.create(secretConfig.getAccessKey(), secretConfig.getSecretKey());
        String token = auth.uploadToken(lookBucket);
        try {
            Response res = uploadManager.put(file, key, token);
            String bodyStr = res.bodyString();
            Map<String,Object> resultMap = JSON.parseObject(bodyStr);
            String k = (String) resultMap.get("key");
            result.setRet(1);
            result.setMsg(k);
        } catch (Exception e) {
            LOGGER.error("七牛上传文件错误", e);
            result.setRet(0);
            result.setMsg("抱歉服务异常,请联系管理员:nhweiwin(微信号)");
        }
        return result;
    }

    public Result upload(File file, String key){
        return upload(file, key, lookBucket);
    }

    public Result upload(String filePath, String key){
        return upload(new File(filePath), key);
    }

    public static class Result{
        private int ret;
        private String msg;

        public int getRet() {
            return ret;
        }

        public void setRet(int ret) {
            this.ret = ret;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

}
