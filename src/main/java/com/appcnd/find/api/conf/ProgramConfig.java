package com.appcnd.find.api.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author nihao 2018/11/23
 */
@Configuration
@ConfigurationProperties(prefix = "config")
@Data
public class ProgramConfig {

    private String weixinAuthUrl;

}
