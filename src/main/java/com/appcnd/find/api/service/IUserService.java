package com.appcnd.find.api.service;

import com.appcnd.find.api.exception.FindException;
import com.appcnd.find.api.pojo.UserVO;
import com.appcnd.find.api.pojo.json.UserInfo;

/**
 * @author nihao 2018/11/22
 */
public interface IUserService {

    UserVO wxLogin(String code, UserInfo userInfo) throws FindException;

    UserVO login(Long id) throws FindException;
}
