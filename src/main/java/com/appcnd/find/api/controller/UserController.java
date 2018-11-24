package com.appcnd.find.api.controller;

import com.appcnd.find.api.exception.FindException;
import com.appcnd.find.api.pojo.json.LoginRes;
import com.appcnd.find.api.pojo.vo.UserVO;
import com.appcnd.find.api.service.IUserService;
import com.appcnd.find.api.util.DesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public String auth(@RequestBody LoginRes loginRes) {
        if (loginRes.getUserInfo() == null || loginRes.getCode() == null) {
            return fail("【请求参数不全】").json();
        }
        try {
            UserVO userVO = userService.wxLogin(loginRes);
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
