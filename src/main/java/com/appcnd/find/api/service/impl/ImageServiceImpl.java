package com.appcnd.find.api.service.impl;

import com.appcnd.find.api.conf.ProgramConfig;
import com.appcnd.find.api.dao.ImageDAO;
import com.appcnd.find.api.pojo.po.ImagePO;
import com.appcnd.find.api.pojo.result.SearchResult;
import com.appcnd.find.api.pojo.vo.ImageVO;
import com.appcnd.find.api.service.IImageService;
import com.appcnd.find.api.util.Strings;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nihao
 * @create 2018/11/24
 **/
@Service
public class ImageServiceImpl implements IImageService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ImageDAO imageDAO;

    @Autowired
    private ProgramConfig config;

    @Override
    public List<ImageVO> random(Integer limit) {
        List<ImagePO> imagePOList = imageDAO.selectRandom(limit);
        return imagePOList.stream().map(po -> {
            ImageVO vo = new ImageVO();
            BeanUtils.copyProperties(po, vo);
            vo.setSrc(vo.getSrc().replace("${img}", "http://img.nihaov.com"));
            vo.setCompressSrc(vo.getCompressSrc().replace("${img}", "http://img.nihaov.com"));
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public SearchResult query(String name, String key, int page, int rows, String sort, String asc) {
        SearchResult result = new SearchResult();
        if(!Strings.isEmpty(key)){
            SolrQuery query = new SolrQuery();
            if(sort != null){
                if(asc != null && asc.equals("asc")){
                    query.setSort(sort, SolrQuery.ORDER.asc);
                }
                else{
                    query.setSort(sort, SolrQuery.ORDER.desc);
                }
            }
            query.set("df", name);
            query.setQuery(key);
            query.setStart((page - 1) * rows);
            query.setRows(rows);
            QueryResponse response = null;
            try {
                response = config.getSolrClient().query(query);
            } catch (IOException e) {
                LOGGER.error("solr search ERROR",e);
            } catch (SolrServerException e) {
                LOGGER.error("solr search ERROR",e);
            }
            if(response != null){
                SolrDocumentList solrDocumentList = response.getResults();
                long recordCount = solrDocumentList.getNumFound();
                List<ImageVO> list = new ArrayList(new Long(recordCount).intValue());
                for (SolrDocument solrDocument : solrDocumentList) {
                    ImageVO imageVO = convert(solrDocument);
                    list.add(imageVO);
                }
                result.setData(list);
                result.setRecordCount(recordCount);
                long pageCount = recordCount / rows;
                if (recordCount % rows > 0) {
                    pageCount ++;
                }
                result.setPageCount(pageCount);
                if(pageCount == 0){
                    result.setCurPage(0);
                }
                else{
                    result.setCurPage(page);
                }
            }
        }
        return result;
    }

    @Override
    public SearchResult query(String key, int page, int rows, String sort, String asc) {
        return query(config.getDefaultQuery(), key, page, rows, sort, asc);
    }

    private ImageVO convert(SolrDocument document){
        ImageVO imageVO = new ImageVO();
        Object id = document.get("id");
        Object title = document.get("image_title");
        Object image_compress_src = document.get("image_compress_src");
        Object image_src = document.get("image_src");
        Object tags = document.get("image_tag");
        Object date = document.get("image_date");
        Object width = document.get("image_width");
        Object height = document.get("image_height");
        if(id!=null){
            imageVO.setId(Long.parseLong(id.toString()));
        }
        if(title!=null){
            imageVO.setTitle((String) title);
        }
        if(image_compress_src!=null){
            imageVO.setCompressSrc((String) image_compress_src);
        }
        if(image_src!=null){
            imageVO.setSrc((String) image_src);
        }
        if(tags!=null){
            imageVO.setTags((List<String>) tags);
        }
        if(width!=null){
            imageVO.setWidth((Integer) width);
        }
        if(height!=null){
            imageVO.setHeight((Integer) height);
        }
        if(date!=null){
            imageVO.setCreatedAt((Date) date);
        }
        return imageVO;
    }
}
