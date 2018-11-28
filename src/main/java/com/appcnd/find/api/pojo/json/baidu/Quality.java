package com.appcnd.find.api.pojo.json.baidu;

import lombok.Data;

/**
 * @author nihao 2018/11/28
 */
@Data
public class Quality {
    private Double blur;// 人脸模糊程度，范围[0~1]，0表示清晰，1表示模糊
    private Double illumination;// 取值范围在[0~255], 表示脸部区域的光照程度 越大表示光照越好
    private Long completeness;// 人脸完整度，0或1, 0为人脸溢出图像边界，1为人脸都在图像边界内
    private Occlusion occlusion;// 人脸各部分遮挡的概率，范围[0~1]，0表示完整，1表示不完整
}
