package com.appcnd.find.api.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author nihao 2018/11/23
 */
public interface IDrawService {

    void drawLook(MultipartFile multipartFile, String word, Integer pos, Integer size,
                  String color, String family, Integer type, Long uid) throws IOException;

}
