package com.appcnd.find.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.appcnd.find.api.dao.IFaceResultDAO;
import com.appcnd.find.api.dao.IUserDAO;
import com.appcnd.find.api.dao.ImageDAO;
import com.appcnd.find.api.exception.FindException;
import com.appcnd.find.api.exception.FindRuntimeException;
import com.appcnd.find.api.pojo.po.FaceResultPO;
import com.appcnd.find.api.pojo.po.ImagePO;
import com.appcnd.find.api.pojo.po.UserPO;
import com.appcnd.find.api.pojo.vo.FaceListVO;
import com.appcnd.find.api.pojo.vo.FaceResultVO;
import com.appcnd.find.api.service.IFaceResultService;
import com.appcnd.find.api.util.QNUtils;
import com.appcnd.find.api.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author nihao
 * @create 2018/12/2
 **/
@Service
public class FaceResultServiceImpl implements IFaceResultService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IFaceResultDAO faceResultDAO;
    @Resource
    private IUserDAO userDAO;
    @Resource
    private ImageDAO imageDAO;
    @Autowired
    private QNUtils qnUtils;

    @Override
    public FaceResultVO getById(Long id) {
        FaceResultPO po = faceResultDAO.selectById(id);
        if (po == null || !po.getFlag() || po.getFaceUrl() == null) {
            return null;
        }
        FaceResultVO vo = new FaceResultVO();
        BeanUtils.copyProperties(po, vo);
        UserPO userPO = userDAO.selectById(po.getUid());
        if (userPO != null) {
            vo.setUserName(userPO.getNickname());
            vo.setHeadPic(userPO.getHeadPic());
        }
        if (vo.getJson() != null) {
            vo.setFace(JSON.parseObject(vo.getJson(), FaceListVO.class));
        }
        vo.getFace().setFaceUrl(Strings.compileUrl(vo.getFaceUrl()));
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String share(Long uid, Long faceResultId, MultipartFile multipartFile) throws FindException {
        FaceResultPO faceResultPO = faceResultDAO.selectById(faceResultId);
        if (faceResultPO == null) {
            throw new FindException("抱歉，服务异常，请稍后再试！");
        }
        if (faceResultPO.getFaceUrl() != null) {
            return Strings.compileUrl(faceResultPO.getFaceUrl());
        }
        String fileName = "faceResult_" + UUID.randomUUID().toString() + multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        //上传七牛云
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
        } catch (IOException e) {
            LOGGER.error("{}", e);
            throw new FindException("抱歉，服务异常，请稍后再试！");
        }
        boolean success = qnUtils.upload(inputStream, fileName, "mydata");
        if (!success) {
            throw new FindException("抱歉，服务异常，请稍后再试！");
        }
        String url = "${mydata}/" + fileName;
        faceResultDAO.updateFaceUrl(faceResultId, url);
        return  Strings.compileUrl(url);
    }
}
