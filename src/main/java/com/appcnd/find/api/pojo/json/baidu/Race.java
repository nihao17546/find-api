package com.appcnd.find.api.pojo.json.baidu;

import lombok.Data;

/**
 * @author nihao 2018/11/28
 */
@Data
public class Race {
    private String type;// yellow: 黄种人 white: 白种人 black:黑种人 arabs: 阿拉伯人
    private Double probability;// 人种置信度，范围【0~1】，0代表概率最小、1代表最大
}
