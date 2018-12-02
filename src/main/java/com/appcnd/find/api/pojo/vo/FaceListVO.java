package com.appcnd.find.api.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @author nihao
 * @create 2018/11/30
 **/
@Data
public class FaceListVO {
    private Integer width;
    private Integer height;
    private String faceUrl;
    private String shareMsg;
    private Long faceResultId;
    private List<FaceVO> faces;
}
