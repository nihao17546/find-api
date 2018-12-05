package com.appcnd.find.api.service;

import com.appcnd.find.api.exception.FindException;
import com.appcnd.find.api.pojo.vo.FaceResultVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author nihao
 * @create 2018/12/2
 **/
public interface IFaceResultService {
    FaceResultVO getById(Long id);
    String share(Long uid, Long faceResultId, MultipartFile multipartFile) throws FindException;
}
