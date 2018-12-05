package com.appcnd.find.api.controller;

import com.appcnd.find.api.dao.IUserDAO;
import com.appcnd.find.api.exception.FindException;
import com.appcnd.find.api.pojo.json.LoginRes;
import com.appcnd.find.api.pojo.vo.UserVO;
import com.appcnd.find.api.service.IFaceResultService;
import com.appcnd.find.api.service.IUserService;
import com.appcnd.find.api.util.DesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author nihao 2018/11/22
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IUserService userService;
    @Resource
    private IUserDAO userDAO;
    @Autowired
    private IFaceResultService faceResultService;

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

    @RequestMapping("/favo")
    public String favo(@Value("#{request.getAttribute('uid')}") Long uid, @RequestParam Long picId) {
        int result = userService.favo(uid, picId);
        if(result==1){
            return ok("收藏成功").json();
        }
        else {
            return ok("已经收藏过了").json();
        }
    }

    @RequestMapping("/rmFavo")
    public String rmFavo(@Value("#{request.getAttribute('uid')}") Long uid, @RequestParam Long picId) {
        userDAO.deleteFavo(uid, picId);
        return ok().json();
    }

    @RequestMapping("/share")
    public String share(@Value("#{request.getAttribute('uid')}") Long uid,
                        @RequestParam Long faceResultId,
                        @RequestParam(value = "file") MultipartFile multipartFile) {
        try {
            String url = faceResultService.share(uid, faceResultId, multipartFile);
            return ok("分享成功").pull("response", url).json();
        } catch (FindException e) {
            return fail(e.getMessage()).json();
        }
    }
}
