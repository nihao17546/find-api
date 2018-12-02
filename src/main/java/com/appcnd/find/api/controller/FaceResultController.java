package com.appcnd.find.api.controller;

import com.appcnd.find.api.pojo.vo.FaceResultVO;
import com.appcnd.find.api.service.IFaceResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nihao
 * @create 2018/12/2
 **/
@RestController
@RequestMapping("/face/result")
public class FaceResultController extends BaseController {

    @Autowired
    private IFaceResultService faceResultService;

    @RequestMapping("/one")
    public String one(@RequestParam Long id) {
        FaceResultVO faceResultVO = faceResultService.getById(id);
        if (faceResultVO == null) {
            return fail("抱歉，未找到对应分享数据！").json();
        }
        return ok().pull("response", faceResultVO).json();
    }

}
