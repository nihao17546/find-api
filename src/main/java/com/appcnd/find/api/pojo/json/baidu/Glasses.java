package com.appcnd.find.api.pojo.json.baidu;

import lombok.Data;

/**
 * @author nihao 2018/11/28
 */
@Data
public class Glasses {
    private String type;// none:无眼镜，common:普通眼镜，sun:墨镜
    private Double probability;// 眼镜置信度，范围【0~1】，0代表概率最小、1代表最大。
}
