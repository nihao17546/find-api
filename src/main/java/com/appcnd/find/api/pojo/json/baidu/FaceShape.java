package com.appcnd.find.api.pojo.json.baidu;

import lombok.Data;

/**
 * @author nihao 2018/11/28
 */
@Data
public class FaceShape {
    private String type;// square: 正方形 triangle:三角形 oval: 椭圆 heart: 心形 round: 圆形
    private Double probability;
}
