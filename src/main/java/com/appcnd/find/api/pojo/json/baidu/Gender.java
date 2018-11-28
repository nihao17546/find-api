package com.appcnd.find.api.pojo.json.baidu;

import lombok.Data;

/**
 * @author nihao 2018/11/28
 */
@Data
public class Gender {
    private String type;// male:男性 female:女性
    private Double probability;// 性别置信度，范围【0~1】，0代表概率最小、1代表最大
}
