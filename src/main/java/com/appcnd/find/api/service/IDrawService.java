package com.appcnd.find.api.service;

import com.appcnd.find.api.exception.FindException;
import com.appcnd.find.api.pojo.vo.FaceListVO;
import com.appcnd.find.api.pojo.vo.ImageVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author nihao 2018/11/23
 */
public interface IDrawService {

    ImageVO drawLook(MultipartFile multipartFile, String word, Integer pos, Integer size,
                     String color, String family, Integer type, Long uid) throws FindException;

    FaceListVO drawFace(MultipartFile multipartFile, Long uid) throws FindException;
}
