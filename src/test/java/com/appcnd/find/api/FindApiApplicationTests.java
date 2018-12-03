package com.appcnd.find.api;

import com.appcnd.find.api.dao.IPhotoDAO;
import com.appcnd.find.api.exception.FindException;
import com.appcnd.find.api.pojo.StaticConstant;
import com.appcnd.find.api.pojo.json.baidu.BaiduResult;
import com.appcnd.find.api.pojo.po.Image2TagPO;
import com.appcnd.find.api.pojo.po.ImagePO;
import com.appcnd.find.api.pojo.po.TagPO;
import com.appcnd.find.api.service.ISolrService;
import com.appcnd.find.api.util.BaiduFaceUtil;
import com.appcnd.find.api.util.BaseUtil;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Autowired
    private BaiduFaceUtil baiduFaceUtil;

    @Test
    public void loadData() {
//        迁移图片数据(1L, 5000L);
//        迁移图片数据(5001L, 10000L);
//        迁移图片数据(10001L, 15000L);
//        迁移图片数据(15001L, 20000L);
//        迁移图片数据(20001L, 25000L);
//        迁移图片数据(25001L, 30000L);
//        迁移图片数据(30001L, 35000L);
        迁移图片数据(35001L, 40000L);
    }

    @Test
    public void loadIndex() throws IOException, SolrServerException {
//        solrService.deleteItem("*:*");
        同步索引(1L, 5000L);
        同步索引(5001L, 10000L);
        同步索引(10001L, 15000L);
        同步索引(15001L, 20000L);
        同步索引(20001L, 25000L);
        同步索引(25001L, 30000L);
        同步索引(30001L, 35000L);
        同步索引(35001L, 40000L);
    }

    @Test
    public void testFace() throws FileNotFoundException, FindException {
        File file = new File("C:\\Users\\admin\\Desktop\\2.jpg");
        String image = BaseUtil.getBase64(new FileInputStream(file));
        BaiduResult result = baiduFaceUtil.detect(image);
        System.out.println("-");
    }

    public void 迁移图片数据(Long from, Long to) {
        List<ImagePO> imagePOList = photoDAO.selectOldImage(from, to);
        List<Image2TagPO> image2TagPOList = null;
        List<TagPO> tagPOList = null;
        if (imagePOList.isEmpty()) {
            return;
        }
        List<Long> imageIds = new ArrayList<>();
        for (ImagePO imagePO : imagePOList) {
            imageIds.add(imagePO.getId());
            if (imagePO.getSrc().contains(StaticConstant.IMG_PREFIX)) {
                imagePO.setSrc(imagePO.getSrc().replace(StaticConstant.IMG_PREFIX, "${img}"));
            }
            else if (imagePO.getSrc().contains(StaticConstant.MYDATA_PREFIX)) {
                imagePO.setSrc(imagePO.getSrc().replace(StaticConstant.MYDATA_PREFIX, "${mydata}"));
            }
            else if (imagePO.getSrc().contains(StaticConstant.FDFS_PREFIX)) {
                imagePO.setSrc(imagePO.getSrc().replace(StaticConstant.FDFS_PREFIX, "${fdfs}"));
            }
            else if (imagePO.getSrc().contains(StaticConstant.ACTIVITY_PREFIX)) {
                imagePO.setSrc(imagePO.getSrc().replace(StaticConstant.ACTIVITY_PREFIX, "${activity}"));
            }

            if (imagePO.getCompressSrc().contains(StaticConstant.IMG_PREFIX)) {
                imagePO.setCompressSrc(imagePO.getCompressSrc().replace(StaticConstant.IMG_PREFIX, "${img}"));
            }
            else if (imagePO.getCompressSrc().contains(StaticConstant.MYDATA_PREFIX)) {
                imagePO.setCompressSrc(imagePO.getCompressSrc().replace(StaticConstant.MYDATA_PREFIX, "${mydata}"));
            }
            else if (imagePO.getCompressSrc().contains(StaticConstant.FDFS_PREFIX)) {
                imagePO.setCompressSrc(imagePO.getCompressSrc().replace(StaticConstant.FDFS_PREFIX, "${fdfs}"));
            }
            else if (imagePO.getCompressSrc().contains(StaticConstant.ACTIVITY_PREFIX)) {
                imagePO.setCompressSrc(imagePO.getCompressSrc().replace(StaticConstant.ACTIVITY_PREFIX, "${activity}"));
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

    public void 同步索引(Long from, Long to) throws IOException, SolrServerException {
        List<Map<String,Object>> list = photoDAO.selectIntoSolr(from, to);
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
                String[] tags = map.get("image_tag").toString().split(",");
                if (tags.length > 0) {
                    solrInputDocument.addField("image_tag", Arrays.asList(tags));
                }
            }
            solrInputDocumentList.add(solrInputDocument);
        }
        solrService.addItem(solrInputDocumentList);
    }
}
