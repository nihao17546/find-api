package com.appcnd.find.api.pojo.json.baidu;

import lombok.Data;

/**
 * @author nihao 2018/11/28
 */
@Data
public class Location {
    private Double left;// 人脸区域离左边界的距离
    private Double top;// 人脸区域离上边界的距离
    private Double width;// 人脸区域的宽度
    private Double height;// 人脸区域的高度
    private Integer rotation;// 人脸框相对于竖直方向的顺时针旋转角，[-180,180]
}
