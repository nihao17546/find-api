package com.appcnd.find.api.controller;

import com.alibaba.fastjson.JSON;
import com.appcnd.find.api.exception.FindException;
import com.appcnd.find.api.pojo.vo.UserVO;
import com.appcnd.find.api.pojo.json.UserInfo;
import com.appcnd.find.api.service.IUserService;
import com.appcnd.find.api.util.DesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nihao 2018/11/22
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IUserService userService;

    @RequestMapping("/auth")
    public String auth(@RequestParam String code, @RequestParam String user) {
        UserInfo userInfo = JSON.parseObject(user, UserInfo.class);
        try {
            UserVO userVO = userService.wxLogin(code, userInfo);
            return ok().pull("response", userVO).json();
        } catch (FindException e) {
            return fail(e.getMessage()).json();
        }
    }

    @RequestMapping("/login")
    public String login(@RequestParam String token) {
        Long id;
        try {
            String id_ = DesUtil.decrypt(token);
            id = Long.parseLong(id_);
        } catch (Exception e) {
            LOGGER.error("登录验证解析错误", e);
            return fail("登录验证失败").json();
        }
        try {
            UserVO userVO = userService.login(id);
            return ok().pull("response", userVO).json();
        } catch (FindException e) {
            return fail(e.getMessage()).json();
        }
    }
}
