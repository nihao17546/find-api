package com.appcnd.find.api.util;

import com.appcnd.find.api.conf.SecretConfig;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author nihao
 * @create 2018/12/3
 **/
@Component
public class QNUtils {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SecretConfig secretConfig;

    private  Auth auth = null;
    private UploadManager uploadManager = null;
    private BucketManager bucketManager = null;

    @PostConstruct
    public void init() {
        auth = Auth.create(secretConfig.getAccessKey(), secretConfig.getSecretKey());
        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);
        uploadManager = new UploadManager(c);
        bucketManager = new BucketManager(auth, c);
    }

    public final boolean upload(String filePath, String key, String bucketname) {
        try {
            uploadManager.put(filePath, key, auth.uploadToken(bucketname));
            return true;
        } catch (QiniuException e) {
            Response r = e.response;
            LOGGER.error("上传文件错误,错误信息: {}", r.toString());
            try {
                LOGGER.error("上传文件错误,响应的文本信息: {}", r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
        return false;
    }

    public final boolean delete(String bucketname, String key) {
        try {
            bucketManager.delete(bucketname, key);
            return true;
        } catch (QiniuException e) {
            LOGGER.error("删除文件错误,bucketname:{},key:{}", bucketname, key);
            LOGGER.error("删除文件错误,错误信息: {}", e.response.toString());
        }
        return false;
    }

}
