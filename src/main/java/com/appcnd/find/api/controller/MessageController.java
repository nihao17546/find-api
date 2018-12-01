package com.appcnd.find.api.controller;

import com.appcnd.find.api.pojo.vo.MessageVO;
import com.appcnd.find.api.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author nihao
 * @create 2018/12/1
 **/
@RequestMapping("/message")
@RestController
public class MessageController extends BaseController {

    @Autowired
    private IMessageService messageService;

    @GetMapping("/list")
    public String list(@Value("#{request.getAttribute('uid')}") Long uid,
                       @RequestParam(required = false, defaultValue = "0") Integer offset,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        List<MessageVO> messageVOS = messageService.list(uid, offset, pageSize);
        return ok().pull("response", messageVOS).json();
    }

}
