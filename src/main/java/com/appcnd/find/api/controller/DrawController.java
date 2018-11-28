package com.appcnd.find.api.controller;

import com.appcnd.find.api.exception.FindException;
import com.appcnd.find.api.pojo.vo.FaceVO;
import com.appcnd.find.api.pojo.vo.ImageVO;
import com.appcnd.find.api.service.IDrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author nihao 2018/11/23
 */
@RestController()
@RequestMapping("/draw")
public class DrawController extends BaseController {

    @Autowired
    private IDrawService drawService;

    @PostMapping("/look")
    public String look(@Value("#{request.getAttribute('uid')}") Long uid,
                       @RequestParam(value = "file") MultipartFile multipartFile,
                       @RequestParam(value = "word") String word,
                       @RequestParam(value = "pos") Integer pos,
                       @RequestParam(value = "size") Integer size,
                       @RequestParam(value = "color") String color,
                       @RequestParam(value = "family", required = false) String family,
                       @RequestParam(value = "type") Integer type){
        try {
            ImageVO imageVO = drawService.drawLook(multipartFile, word, pos, size, color, family, type, uid);
            return ok().pull("response", imageVO).json();
        } catch (FindException e) {
            return fail(e.getMessage()).json();
        }
    }

    @PostMapping("/face")
    public String face(@Value("#{request.getAttribute('uid')}") Long uid,
                       @RequestParam(value = "file") MultipartFile multipartFile) {
        try {
            List<FaceVO> faceVOList = drawService.drawFace(multipartFile, uid);
            return ok().pull("response", faceVOList).json();
        } catch (FindException e) {
            return fail(e.getMessage()).json();
        }
    }

}
