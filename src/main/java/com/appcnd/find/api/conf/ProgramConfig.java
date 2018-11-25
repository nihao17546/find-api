package com.appcnd.find.api.conf;

import lombok.Data;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author nihao 2018/11/23
 */
@Configuration
@ConfigurationProperties(prefix = "config")
@Data
public class ProgramConfig {

    private String weixinAuthUrl;
    private String[] filterUris;
    private String defaultQuery;
    private String coreUrl;
    private SolrClient solrClient;

    public void setFilterUris(String filterUris) {
        if (filterUris != null && !filterUris.equals("")) {
            this.filterUris = filterUris.split(",");
        }
        else {
            this.filterUris = new String[]{};
        }
    }

    public void setCoreUrl(String coreUrl) {
        this.coreUrl = coreUrl;
        solrClient = new HttpSolrClient.Builder(coreUrl).build();
    }
}
