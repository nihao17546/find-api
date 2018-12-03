package com.appcnd.find.api;

import com.appcnd.find.api.util.BaseUtil;
import com.baidu.aip.face.AipFace;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * @author nihao 2018/11/28
 */
public class SimpleTest {

    @Test
    public void test01() throws JSONException, FileNotFoundException {
        //设置需要操作的账号的AK和SK
        String ACCESS_KEY = "";
        String SECRET_KEY = "";
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

        Zone z = Zone.zone0();
        Configuration c = new Configuration(z);

        //实例化一个BucketManager对象
        BucketManager bucketManager = new BucketManager(auth, c);
        //要测试的空间和key，并且这个key在你空间中存在
        String bucket = "activity";
        String key = "qq.html";
        try {
            //调用delete方法移动文件
            Response response = bucketManager.delete(bucket, key);
            System.out.println("--");
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            System.out.println(r.toString());

        }
    }

}
