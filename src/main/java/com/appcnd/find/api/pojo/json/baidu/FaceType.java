package com.appcnd.find.api.pojo.json.baidu;

import lombok.Data;

/**
 * @author nihao 2018/11/28
 */
@Data
public class FaceType {
    private String type;// human: 真实人脸 cartoon: 卡通人脸
    private Double probability;// 	人脸类型判断正确的置信度，范围【0~1】，0代表概率最小、1代表最大
}
