package com.appcnd.find.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author nihao 2018/11/23
 */
@RestController("/draw")
public class DrawController extends BaseController {

    @PostMapping("/look")
    public String look(@Value("#{request.getAttribute('uid')}") Long uid,
                       @RequestParam(value = "file",required = true) MultipartFile multipartFile,
                       @RequestParam(value = "word", required = true) String word,
                       @RequestParam(value = "pos", required = true) Integer pos,
                       @RequestParam(value = "size", required = true) Integer size,
                       @RequestParam(value = "color", required = true) String color,
                       @RequestParam(value = "family", required = false) String family,
                       @RequestParam(value = "type", required = true) Integer type){
        return null;
    }

}
