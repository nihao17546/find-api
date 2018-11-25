package com.appcnd.find.api;

import com.appcnd.find.api.dao.IPhotoDAO;
import com.appcnd.find.api.pojo.po.Image2TagPO;
import com.appcnd.find.api.pojo.po.ImagePO;
import com.appcnd.find.api.pojo.po.TagPO;
import com.appcnd.find.api.service.ISolrService;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan("com.appcnd.find.api.dao")
public class FindApiApplicationTests {

    @Resource
    private IPhotoDAO photoDAO;
    @Autowired
    private ISolrService solrService;

    @Test
    public void contextLoads() throws IOException, SolrServerException {
        fun();
    }

    @Test
    public void ascasvca() throws IOException, SolrServerException {
        List<Map<String,Object>> list = photoDAO.selectIntoSolr(0L, 1000L);
        List<SolrInputDocument> solrInputDocumentList = new ArrayList<>();
        for (Map<String,Object> map : list) {
            SolrInputDocument solrInputDocument = new SolrInputDocument();
            solrInputDocument.addField("id", Long.parseLong(map.get("id").toString()));
            solrInputDocument.addField("image_title", map.get("title").toString());
            solrInputDocument.addField("image_compress_src", map.get("compress_src").toString());
            solrInputDocument.addField("image_src", map.get("src").toString());
            solrInputDocument.addField("image_date", map.get("created_at"));
            solrInputDocument.addField("image_width", map.get("width"));
            solrInputDocument.addField("image_height", map.get("height"));
            solrInputDocument.addField("image_dih_time", map.get("dih_time"));
            if (map.get("image_tag") != null) {
                solrInputDocument.addField("image_tag", map.get("image_tag").toString().split(","));
            }
            solrInputDocumentList.add(solrInputDocument);
        }
        solrService.addItem(solrInputDocumentList);
    }

    public void fun() throws IOException, SolrServerException {
        for (int i = 0; i <= 100; i ++) {
            List<SolrInputDocument> solrInputDocumentList = new ArrayList<>();
            List<ImagePO> imagePOList = photoDAO.selectOldImage(i * 1000L, (i + 1) * 1000L - 1);
            List<Image2TagPO> image2TagPOList = null;
            List<TagPO> tagPOList = null;
            if (imagePOList.isEmpty()) {
                break;
            }
            List<Long> imageIds = new ArrayList<>();
            for (ImagePO imagePO : imagePOList) {
                imageIds.add(imagePO.getId());
                if (imagePO.getSrc().contains("http://img.nihaov.com")) {
                    imagePO.setSrc(imagePO.getSrc().replace("http://img.nihaov.com", "${img}"));
                }
                else if (imagePO.getSrc().contains("http://mydata.appcnd.com")) {
                    imagePO.setSrc(imagePO.getSrc().replace("http://mydata.appcnd.com", "${mydata}"));
                }
                else if (imagePO.getSrc().contains("http://fdfs.nihaov.com")) {
                    imagePO.setSrc(imagePO.getSrc().replace("http://fdfs.nihaov.com", "${fdfs}"));
                }
                else if (imagePO.getSrc().contains("http://activity.appcnd.com")) {
                    imagePO.setSrc(imagePO.getSrc().replace("http://activity.appcnd.com", "${activity}"));
                }

                if (imagePO.getCompressSrc().contains("http://img.nihaov.com")) {
                    imagePO.setCompressSrc(imagePO.getCompressSrc().replace("http://img.nihaov.com", "${img}"));
                }
                else if (imagePO.getCompressSrc().contains("http://mydata.appcnd.com")) {
                    imagePO.setCompressSrc(imagePO.getCompressSrc().replace("http://mydata.appcnd.com", "${mydata}"));
                }
                else if (imagePO.getCompressSrc().contains("http://fdfs.nihaov.com")) {
                    imagePO.setCompressSrc(imagePO.getCompressSrc().replace("http://fdfs.nihaov.com", "${fdfs}"));
                }
                else if (imagePO.getCompressSrc().contains("http://activity.appcnd.com")) {
                    imagePO.setCompressSrc(imagePO.getCompressSrc().replace("http://activity.appcnd.com", "${activity}"));
                }
            }
            if (!imageIds.isEmpty()) {
                image2TagPOList = photoDAO.selectOldImage2TagByImageIds(imageIds);
                List<Long> tagIds = new ArrayList<>();
                for (Image2TagPO image2TagPO : image2TagPOList) {
                    if (!tagIds.contains(image2TagPO.getTagId())) {
                        tagIds.add(image2TagPO.getTagId());
                    }
                }
                if (!tagIds.isEmpty()) {
                    tagPOList = photoDAO.selectOldTagByIds(tagIds);
                }
            }
            photoDAO.insertNewImage(imagePOList);
            if (image2TagPOList != null && !image2TagPOList.isEmpty()) {
                photoDAO.insertNewImage2Tag(image2TagPOList);
            }
            if (tagPOList != null && !tagPOList.isEmpty()) {
                photoDAO.insertNewTag(tagPOList);
            }
        }
    }
}
