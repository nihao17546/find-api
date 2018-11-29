package com.appcnd.find.api.pojo.vo;

import lombok.Data;

/**
 * Created by nihao on 17/9/8.
 */
@Data
public class FaceVO {
    private String gender;// 性别
    private Integer age;// 年龄
    private Integer beauty;// 颜值
    private String expression;// 表情
    private String faceShape;// 脸型
    private String glasses;// 眼镜
    private String race;// 人种肤色
    private String faceType;// 卡通还是真人
}
