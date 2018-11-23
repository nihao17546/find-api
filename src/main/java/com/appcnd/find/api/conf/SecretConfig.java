package com.appcnd.find.api.conf;

import com.appcnd.find.api.dao.ISecretDAO;
import com.appcnd.find.api.pojo.po.SecretPO;
import lombok.Data;
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

    private String accessKey = null;
    private String secretKey = null;
    private String appId = null;
    private String appSecret = null;
    private String baiduAppId = null;
    private String baiduSecretKey = null;

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
    }
}
