package com.appcnd.find.api.service.impl;

import com.appcnd.find.api.dao.ImageDAO;
import com.appcnd.find.api.exception.FindException;
import com.appcnd.find.api.pojo.json.FontText;
import com.appcnd.find.api.pojo.po.ImagePO;
import com.appcnd.find.api.pojo.vo.ImageVO;
import com.appcnd.find.api.service.IDrawService;
import com.appcnd.find.api.util.AnimatedGifEncoder;
import com.appcnd.find.api.util.ImageUtils;
import com.appcnd.find.api.util.QINIUUtils;
import com.appcnd.find.api.util.SimpleDateUtil;
import com.appcnd.find.api.util.Strings;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.UUID;

/**
 * @author nihao 2018/11/23
 */
@Service
public class DrawServiceImpl implements IDrawService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QINIUUtils qiniuUtils;
    @Resource
    private ImageDAO imageDAO;

    @Override
    public ImageVO drawLook(MultipartFile multipartFile, String word, Integer pos, Integer size,
                         String color, String family, Integer type, Long uid) throws FindException {
        String today = SimpleDateUtil.shortFormat(new Date()).replaceAll("-","");
        String sourcePath = "/mydata/ftp/look/source/" + today + "/" + UUID.randomUUID().toString() + "-" + multipartFile.getOriginalFilename();
        String outPath = "/mydata/ftp/look/out/" + today;
        File sourceFile = new File(sourcePath);
        if(!sourceFile.getParentFile().exists()){
            sourceFile.getParentFile().mkdirs();
        }
        File outFile = new File(outPath);
        if(!outFile.exists()){
            outFile.mkdirs();
        }
        try {
            String filePath = null, fileName = null;
            FontText fontText = new FontText(word, pos, color, size, family);
            Thumbnails.of(multipartFile.getInputStream()).size(500,500).toFile(sourceFile);
            ImageIcon imgIcon = new ImageIcon(sourcePath);
            Image img = imgIcon.getImage();
            if (type == 1) { //固定文字
                fileName = UUID.randomUUID().toString() + ".png";
                filePath = outPath + "/" + fileName;
                BufferedImage bufferedImage = ImageUtils.drawTextInImg(img, fontText, 0);
                FileOutputStream out = new FileOutputStream(filePath);
                ImageIO.write(bufferedImage, "png", out);
                out.close();
            }
            else{
                fileName = UUID.randomUUID().toString() + ".gif";
                filePath = outPath+ "/" + fileName;
                AnimatedGifEncoder animatedGifEncoder = new AnimatedGifEncoder();
                BufferedImage bufferedImage1 = ImageUtils.drawTextInImg(img, fontText, 10);
                BufferedImage bufferedImage2 = ImageUtils.drawTextInImg(img, fontText, 0);
                animatedGifEncoder.start(filePath);
                animatedGifEncoder.setRepeat(0);
                animatedGifEncoder.addFrame(bufferedImage1);
                animatedGifEncoder.setDelay(500);
                animatedGifEncoder.addFrame(bufferedImage2);
                animatedGifEncoder.setDelay(500);
                animatedGifEncoder.finish();
            }
            //上传文件到七牛
            QINIUUtils.Result result = qiniuUtils.upload(filePath, fileName);
            if(result.getRet() == 1){
                String url = QINIUUtils.URL_PREFIX + result.getMsg();
                ImagePO imagePO = new ImagePO();
                imagePO.setTitle("用户上传图片" + uid);
                imagePO.setCompressSrc(url);
                imagePO.setSrc(url);
                int width = img.getWidth(null);
                int height = img.getHeight(null);
                imagePO.setWidth(width);
                imagePO.setHeight(height);
                imagePO.setUid(uid);
                int r = imageDAO.insertPic(imagePO);
                if(r == 1){
//                        userService.favo(uid ,imgpo.getId());
                }
                ImageVO imageVO = new ImageVO();
                BeanUtils.copyProperties(imagePO, imageVO);
                imageVO.setSrc(Strings.compileUrl(imageVO.getSrc()));
                imageVO.setCompressSrc(Strings.compileUrl(imageVO.getCompressSrc()));
                return imageVO;
            }
            else{
                throw new FindException(result.getMsg());
            }
        } catch (FindException e) {
          throw e;
        } catch (Exception e) {
            LOGGER.error("{}", e);
            throw new FindException(e.getMessage());
        }
    }
}
