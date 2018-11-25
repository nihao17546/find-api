package com.appcnd.find.api.service.impl;

import com.appcnd.find.api.conf.ProgramConfig;
import com.appcnd.find.api.service.ISolrService;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author nihao
 * @create 2018/11/25
 **/
@Service
public class SolrServiceImpl implements ISolrService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProgramConfig config;

    @Override
    public void addItem(SolrInputDocument solrInputDocument) throws IOException, SolrServerException {
        config.getSolrClient().add(solrInputDocument);
        config.getSolrClient().commit();
    }

    @Override
    public void addItem(List<SolrInputDocument> solrInputDocuments) throws IOException, SolrServerException {
        config.getSolrClient().add(solrInputDocuments);
        config.getSolrClient().commit();
    }

    @Override
    public void deleteItem(String query) throws IOException, SolrServerException {
        config.getSolrClient().deleteByQuery(query);
        config.getSolrClient().commit();
    }
}
