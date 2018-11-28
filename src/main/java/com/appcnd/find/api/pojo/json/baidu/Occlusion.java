package com.appcnd.find.api.pojo.json.baidu;

import lombok.Data;

/**
 * @author nihao 2018/11/28
 */
@Data
public class Occlusion {
    private Double left_eye;// 左眼遮挡比例
    private Double right_eye;// 右眼遮挡比例
    private Double nose;// 鼻子遮挡比例
    private Double mouth;// 嘴巴遮挡比例
    private Double left_cheek;// 左脸颊遮挡比例
    private Double right_cheek;// 右脸颊遮挡比例
    private Double chin;// 下巴遮挡比例
}
