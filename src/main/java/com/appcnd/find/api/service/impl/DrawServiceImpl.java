package com.appcnd.find.api.service.impl;

import com.appcnd.find.api.pojo.json.FontText;
import com.appcnd.find.api.service.IDrawService;
import com.appcnd.find.api.util.SimpleDateUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @author nihao 2018/11/23
 */
@Service
public class DrawServiceImpl implements IDrawService {
    @Override
    public void drawLook(MultipartFile multipartFile, String word, Integer pos, Integer size,
                         String color, String family, Integer type, Long uid) throws IOException {
        String today = SimpleDateUtil.shortFormat(new Date()).replaceAll("-","");
        String sourcePath = "/mydata/ftp/look/source/" + today + "/" + UUID.randomUUID().toString() + "-" + multipartFile.getOriginalFilename();
        String outPath = "/mydata/ftp/look/out/" + today;
        File sourceFile = new File(sourcePath);
        if(!sourceFile.getParentFile().exists()){
            sourceFile.getParentFile().mkdirs();
        }
        File outFile = new File(outPath);
        if(!outFile.exists()){
            outFile.mkdirs();
        }
        FontText fontText = new FontText(word, pos, color, size, family);
        Thumbnails.of(multipartFile.getInputStream()).size(500,500).toFile(sourceFile);
    }
}
