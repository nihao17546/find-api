package com.appcnd.find.api.service;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.List;

/**
 * @author nihao
 * @create 2018/11/25
 **/
public interface ISolrService {
    void addItem(SolrInputDocument solrInputDocument) throws IOException, SolrServerException;
    void addItem(List<SolrInputDocument> solrInputDocuments) throws IOException, SolrServerException;
    void deleteItem(String query) throws IOException, SolrServerException;
}
