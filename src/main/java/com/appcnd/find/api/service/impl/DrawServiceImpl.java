package com.appcnd.find.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.appcnd.find.api.dao.IFaceResultDAO;
import com.appcnd.find.api.dao.IUserDAO;
import com.appcnd.find.api.dao.ImageDAO;
import com.appcnd.find.api.exception.FindException;
import com.appcnd.find.api.pojo.StaticConstant;
import com.appcnd.find.api.pojo.json.FontText;
import com.appcnd.find.api.pojo.json.baidu.BaiduResult;
import com.appcnd.find.api.pojo.json.baidu.Face;
import com.appcnd.find.api.pojo.po.FaceResultPO;
import com.appcnd.find.api.pojo.po.ImagePO;
import com.appcnd.find.api.pojo.po.UserFavoPO;
import com.appcnd.find.api.pojo.vo.FaceListVO;
import com.appcnd.find.api.pojo.vo.FaceVO;
import com.appcnd.find.api.pojo.vo.ImageVO;
import com.appcnd.find.api.service.IDrawService;
import com.appcnd.find.api.util.*;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    @Resource
    private IUserDAO userDAO;
    @Resource
    private IFaceResultDAO faceResultDAO;
    @Autowired
    private BaiduFaceUtil baiduUtils;
    @Autowired
    private QNUtils qnUtils;

    @Value("${face.file.path}")
    private String faceFilePath;

    @Transactional(rollbackFor = Exception.class)
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
                imagePO.setFlag(2);
                int r = imageDAO.insertPic(imagePO);
                if(r == 1){
                    userDAO.insertFavo(new UserFavoPO(uid, imagePO.getId()));
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
        } catch (FileNotFoundException e) {
            LOGGER.error("{}", e);
            throw new FindException("抱歉，服务异常，请稍后再试！");
        } catch (IOException e) {
            LOGGER.error("{}", e);
            throw new FindException("抱歉，服务异常，请稍后再试！");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FaceListVO drawFace(MultipartFile multipartFile, Long uid) throws FindException {
        String today = SimpleDateUtil.shortFormat(new Date()).replaceAll("-","");
        String fileName = UUID.randomUUID().toString() + multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        String sourcePath = faceFilePath.replace("{today}", today)
                .replace("{uid}", uid.toString()).replace("{fileName}", fileName);
        File file = new File(sourcePath);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }

        Integer width = null, height = null;
        FaceListVO faceListVO = null;
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byte[] bytes = multipartFile.getBytes();
            String imageBase64 = BaseUtil.getBase64(bytes);
            FileUtils.writeByteArrayToFile(file, bytes, true);
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            width = bufferedImage.getWidth(null);
            height = bufferedImage.getHeight(null);
            //图像识别
            BaiduResult baiduResult = baiduUtils.detect(imageBase64);
            List<FaceVO> faceVOS = new ArrayList<>();
            faceListVO = new FaceListVO();
            faceListVO.setWidth(width);
            faceListVO.setHeight(height);
            if (baiduResult.getFace_num() > 0) {
                int num = 65;
                for (Face face : baiduResult.getFace_list()) {
                    FaceVO faceVO = new FaceVO();
                    faceVO.setNum(((char) num ++) + "");
                    faceVO.setGender(face.getGender().getType());
                    faceVO.setAge(face.getAge().intValue());
                    faceVO.setBeauty(face.getBeauty().intValue());
                    faceVO.setExpression(face.getExpression().getType());
                    faceVO.setFaceShape(face.getFace_shape().getType());
                    faceVO.setGlasses(face.getGlasses().getType());
                    faceVO.setRace(face.getRace().getType());
                    faceVO.setFaceType(face.getFace_type().getType());
                    faceVO.setLocation(face.getLocation());
                    faceVO.setDescription();
                    faceVOS.add(faceVO);
                }
            }
            faceListVO.setFaces(faceVOS);
        } catch (IOException e) {
            LOGGER.error("{}", e);
            throw new FindException("抱歉，服务异常，请稍后再试！");
        } finally {
            if (byteArrayInputStream != null) {
                BaseUtil.closeInputStream(byteArrayInputStream);
            }
        }

        //上传七牛云
//        String key = sourcePath.replace("/mydata/ftp/", "");
//        boolean success = qnUtils.upload(sourcePath, key, "mydata");
//        if (!success) {
//            throw new FindException("抱歉，服务异常，请稍后再试！");
//        }

        //数据库存储
        String url = sourcePath.replace("/mydata/ftp", "${fdfs}");
        ImagePO imagePO = new ImagePO();
        imagePO.setTitle("用户上传照片" + uid);
        imagePO.setCompressSrc(url);
        imagePO.setSrc(url);
        imagePO.setWidth(width);
        imagePO.setHeight(height);
        imagePO.setUid(uid);
        imagePO.setFlag(2);
        imageDAO.insertPic(imagePO);

        FaceResultPO faceResultPO = new FaceResultPO();
        faceResultPO.setUid(uid);
        faceResultPO.setJson(JSON.toJSONString(faceListVO));
        faceResultPO.setPicId(imagePO.getId());
        faceResultDAO.insert(faceResultPO);

        faceListVO.setFaceResultId(faceResultPO.getId());
        faceListVO.setFaceUrl(url);
        faceListVO.setShareMsg("快来围观");
        return faceListVO;
    }
}
