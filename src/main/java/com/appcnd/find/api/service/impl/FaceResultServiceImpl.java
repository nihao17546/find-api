package com.appcnd.find.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.appcnd.find.api.dao.IFaceResultDAO;
import com.appcnd.find.api.dao.IUserDAO;
import com.appcnd.find.api.dao.ImageDAO;
import com.appcnd.find.api.pojo.po.FaceResultPO;
import com.appcnd.find.api.pojo.po.ImagePO;
import com.appcnd.find.api.pojo.po.UserPO;
import com.appcnd.find.api.pojo.vo.FaceListVO;
import com.appcnd.find.api.pojo.vo.FaceResultVO;
import com.appcnd.find.api.service.IFaceResultService;
import com.appcnd.find.api.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author nihao
 * @create 2018/12/2
 **/
@Service
public class FaceResultServiceImpl implements IFaceResultService {

    @Resource
    private IFaceResultDAO faceResultDAO;
    @Resource
    private IUserDAO userDAO;
    @Resource
    private ImageDAO imageDAO;

    @Override
    public FaceResultVO getById(Long id) {
        FaceResultPO po = faceResultDAO.selectById(id);
        if (po == null || !po.getFlag()) {
            return null;
        }
        ImagePO imagePO = imageDAO.selectById(po.getPicId());
        if (imagePO == null) {
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
        vo.getFace().setFaceUrl(Strings.compileUrl(imagePO.getSrc()));
        return vo;
    }
}
