package com.appcnd.find.api.util;

import com.alibaba.fastjson.JSON;
import com.appcnd.find.api.conf.SecretConfig;
import com.appcnd.find.api.exception.FindException;
import com.appcnd.find.api.pojo.json.baidu.BaiduResult;
import com.baidu.aip.face.AipFace;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

/**
 * @author nihao 2018/11/28
 */
@Component
public class BaiduFaceUtil {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SecretConfig secretConfig;

    private HashMap<String, String> options = null;
    private AipFace client = null;

    @PostConstruct
    public void init() {
        options = new HashMap<>();
        options.put("face_field", "age,beauty,expression,faceshape,gender,glasses,race,face_type,landmark,landmark,quality");
        options.put("max_face_num", "10");
        options.put("face_type", "LIVE");
        client = new AipFace(secretConfig.getBaiduFaceAppId(), secretConfig.getBaiduFaceAppkey(), secretConfig.getBaiduFaceSecretKey());
        client.setConnectionTimeoutInMillis(10000);
        client.setSocketTimeoutInMillis(60000);
    }


    public final BaiduResult detect(String image) throws FindException {
        JSONObject jsonObject = client.detect(image, "BASE64", options);
        if (!jsonObject.isNull("result")) {
            try {
                BaiduResult baiduResult = JSON.parseObject(jsonObject.get("result").toString(), BaiduResult.class);
                return baiduResult;
            } catch (Exception e) {
                LOGGER.error("百度人脸检测结果解析失败，响应结果：{}", jsonObject.toString());
                throw new FindException("抱歉，服务异常，请稍后再试！");
            }
        }
        else if (!jsonObject.isNull("error_code") && jsonObject.get("error_code").toString().equals("222202")) {
            return new BaiduResult();
        }
        LOGGER.error("百度人脸检测响应结果：{}", jsonObject.toString());
        throw new FindException("抱歉，服务异常，请稍后再试！");
    }

}
