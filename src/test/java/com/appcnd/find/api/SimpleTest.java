package com.appcnd.find.api;

import com.alibaba.fastjson.JSON;
import com.appcnd.find.api.pojo.json.baidu.Baidu;
import com.appcnd.find.api.util.BaseUtil;
import com.baidu.aip.face.AipFace;
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
        // 初始化一个AipFace
        AipFace client = new AipFace("10107005", "Gy4QSEYxH2DXnqGhsuLYZUK4", "62QnyknN2bcNob3xqMhvcFi2hX2RXpzy");

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
//        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("face_field", "age,beauty,expression,faceshape,gender,glasses,race,face_type,landmark,landmark,quality,");
        options.put("max_face_num", "2");
        options.put("face_type", "LIVE");

        File file = new File("C:\\Users\\admin\\Desktop\\timg.jpg");
        String image = BaseUtil.getBase64(new FileInputStream(file), false);
//        String image = "取决于image_type参数，传入BASE64字符串或URL字符串或FACE_TOKEN字符串";
        String imageType = "BASE64";

        // 人脸检测
        JSONObject res = client.detect(image, imageType, options);
        Baidu baidu = JSON.parseObject(res.get("result").toString(), Baidu.class);
        System.out.println(res.toString(2));
    }

}
