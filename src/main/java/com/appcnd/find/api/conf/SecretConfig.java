package com.appcnd.find.api.conf;

import com.appcnd.find.api.dao.ISecretDAO;
import com.appcnd.find.api.pojo.po.SecretPO;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * @author nihao 2018/11/22
 */
@Configuration
@Data
public class SecretConfig {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private String accessKey = null;
    private String secretKey = null;
    private String appId = null;
    private String appSecret = null;
    private String baiduAppId = null;
    private String baiduSecretKey = null;
    private String baiduFaceAppId = null;
    private String baiduFaceAppkey = null;
    private String baiduFaceSecretKey = null;

    @Resource
    private ISecretDAO secretDAO;

    @PostConstruct
    public void init() {
        Map<String,SecretPO> map = secretDAO.selectAll();
        accessKey = map.get("七牛accessKey").getValue().trim();
        secretKey = map.get("七牛secretKey").getValue().trim();
        appId = map.get("微信appId").getValue().trim();
        appSecret = map.get("微信appSecret").getValue().trim();
        baiduAppId = map.get("百度appId").getValue().trim();
        baiduSecretKey = map.get("百度secretKey").getValue().trim();
        baiduFaceAppId = map.get("baidu#face#appId").getValue().trim();
        baiduFaceAppkey = map.get("baidu#face#appKey").getValue().trim();
        baiduFaceSecretKey = map.get("baidu#face#secretKey").getValue().trim();
        LOGGER.info("secret 配置 -> {}", this);
    }

    @Override
    public String toString() {
        return "SecretConfig{" +
                "accessKey='" + accessKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", appId='" + appId + '\'' +
                ", appSecret='" + appSecret + '\'' +
                ", baiduAppId='" + baiduAppId + '\'' +
                ", baiduSecretKey='" + baiduSecretKey + '\'' +
                ", baiduFaceAppId='" + baiduFaceAppId + '\'' +
                ", baiduFaceAppkey='" + baiduFaceAppkey + '\'' +
                ", baiduFaceSecretKey='" + baiduFaceSecretKey + '\'' +
                '}';
    }
}
