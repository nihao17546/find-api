package com.appcnd.find.api.controller;

import com.appcnd.find.api.service.IHotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author nihao
 * @create 2018/11/24
 **/
@RestController
@RequestMapping("/hot")
public class HotController extends BaseController {

    @Autowired
    private IHotService hotService;

    @GetMapping("/list")
    public String list() {
        List<String> list = hotService.getHots(10);
        return ok().pull("response", list).json();
    }
}
