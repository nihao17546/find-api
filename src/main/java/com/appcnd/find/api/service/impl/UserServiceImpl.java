package com.appcnd.find.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.appcnd.find.api.conf.ProgramConfig;
import com.appcnd.find.api.conf.SecretConfig;
import com.appcnd.find.api.dao.IUserDAO;
import com.appcnd.find.api.exception.FindException;
import com.appcnd.find.api.pojo.json.LoginRes;
import com.appcnd.find.api.pojo.vo.UserVO;
import com.appcnd.find.api.pojo.json.UserInfo;
import com.appcnd.find.api.pojo.po.UserPO;
import com.appcnd.find.api.service.IUserService;
import com.appcnd.find.api.util.DesUtil;
import com.appcnd.find.api.util.HttpClientUtils;
import com.appcnd.find.api.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author nihao 2018/11/22
 */
@Service
public class UserServiceImpl implements IUserService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IUserDAO userDAO;

    @Autowired
    private ProgramConfig programConfig;

    @Autowired
    private SecretConfig secretConfig;

    @Override
    public UserVO wxLogin(LoginRes loginRes) throws FindException {
        UserInfo userInfo = loginRes.getUserInfo();
        Map<String,Object> param = new HashMap<>();
        param.put("appid", secretConfig.getAppId());
        param.put("secret", secretConfig.getAppSecret());
        param.put("js_code", loginRes.getCode());
        param.put("grant_type", "authorization_code");
        String ret = HttpClientUtils.doPost(programConfig.getWeixinAuthUrl(), param);
        Map<String,String> result = JSON.parseObject(ret, Map.class);
        if (result.containsKey("openid")) {
            String openid = result.get("openid");
            UserPO userPO = new UserPO();
            userPO.setNickname(userInfo.getNickName());
            userPO.setUnionId(openid);
            userPO.setHeadPic(userInfo.getAvatarUrl());
            userPO.setGender(userInfo.getGender());
            userPO.setCountry(userInfo.getCountry());
            userPO.setCity(userInfo.getCity());
            userPO.setProvince(userInfo.getProvince());
            UserPO checkPO = userDAO.selectByUnionId(userPO.getUnionId());
            if (checkPO == null) {
                userDAO.insert(userPO);
            }
            else if(needUpdate(userPO, checkPO)){
                userDAO.updateByUnionId(userPO);
            }
            if (userPO.getId() == null) {
                userPO.setId(checkPO.getId());
            }
            return getUserVO(userPO);
        }
        else if (result.containsKey("errmsg")) {
            throw new FindException(result.get("errmsg"));
        }
        else {
            throw new FindException("授权登录失败");
        }
    }

    @Override
    public UserVO login(Long id) throws FindException {
        UserPO userPO = userDAO.selectById(id);
        if (userPO == null) {
            throw new FindException("登录验证失败");
        }
        return getUserVO(userPO);
    }

    private UserVO getUserVO(UserPO userPO) throws FindException {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userPO, userVO);
        String token;
        try {
            token = DesUtil.encrypt(userVO.getId().toString());
        } catch (Exception e) {
            LOGGER.error("加密 {} 错误", userVO.getId().toString(), e);
            throw new FindException("授权登录失败");
        }
        userVO.setToken(token);
        return userVO;
    }

    private boolean needUpdate(UserPO newUserPO, UserPO oldUserPO) {
        if(!Strings.equals(newUserPO.getCity(), oldUserPO.getCity())){
            return true;
        }
        else if(!Strings.equals(newUserPO.getCountry(), oldUserPO.getCountry())){
            return true;
        }
        else if(!Strings.equals(newUserPO.getProvince(), oldUserPO.getProvince())){
            return true;
        }
        else if(!Strings.equals(newUserPO.getHeadPic(), oldUserPO.getHeadPic())){
            return true;
        }
        else if(!Strings.equals(newUserPO.getNickname(), oldUserPO.getNickname())){
            return true;
        }
        else if(newUserPO.getGender() != oldUserPO.getGender()){
            return true;
        }
        return false;
    }
}
