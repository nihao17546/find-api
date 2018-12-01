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
    private List<FaceVO> faces;
}
