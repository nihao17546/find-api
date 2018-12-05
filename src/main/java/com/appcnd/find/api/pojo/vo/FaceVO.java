package com.appcnd.find.api.pojo.vo;

import com.appcnd.find.api.pojo.json.baidu.Location;
import lombok.Data;

import java.util.Random;

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
    private Location location;
    private String num;
    private String description;

    public void setDescription() {
        StringBuilder sb = new StringBuilder();
        if ("human".equals(faceType)) {
            if ("male".equals(gender)) {
                if (age <= 10) {
                    sb.append("这个小男孩").append(age).append("岁");
                }
                else if (age <= 16){
                    sb.append("这个小伙子").append(age).append("岁");
                }
                else if (age <= 30){
                    sb.append("这个帅哥").append(age).append("岁");
                }
                else if (age <= 55){
                    sb.append("这位大叔").append(age).append("岁");
                }
                else {
                    sb.append("这位老爷爷").append(age).append("岁");
                }
            }
            else if ("female".equals(gender)) {
                if (age <= 10) {
                    sb.append("这个小姑娘").append(age).append("岁");
                }
                else if (age <= 16){
                    sb.append("这个小女孩").append(age).append("岁");
                }
                else if (age <= 30){
                    sb.append("这位美女").append(age).append("岁");
                }
                else if (age <= 55){
                    sb.append("这位大婶").append(age).append("岁");
                }
                else {
                    sb.append("这位老奶奶").append(age).append("岁");
                }
            }

            if ("yellow".equals(race)) {
                sb.append("，黄皮肤");
            }
            else if ("white".equals(race)) {
                sb.append("，白种人");
            }
            else if ("black".equals(race)) {
                sb.append("，黑皮肤");
            }
            else if ("arabs".equals(race)) {
                sb.append("，阿拉伯人");
            }

            if ("smile".equals(expression)) {
                sb.append("，面带微笑");
            }
            else if ("laugh".equals(expression)) {
                sb.append("，非常高兴");
            }

            if ("sun".equals(glasses)) {
                sb.append("，戴着一副酷毙了的墨镜");
            }
            else if ("common".equals(glasses)) {
                sb.append("，戴着一副眼镜");
            }

            if (beauty >= 70) {
                if ("male".equals(gender)) {
                    sb.append("，简直是帅到极品！");
                }
                else {
                    sb.append("，简直是颜值爆表！");
                }
            }
            else if (beauty >= 40) {
                sb.append("，综合颜值评分").append(beauty + new Random().nextInt(16)).append("。");
            }
            else if (beauty >= 30) {
                sb.append("，综合颜值评分").append(beauty + new Random().nextInt(10)).append("。");
            }
            else {
                sb.append("，长得有点着急哦。");
            }
        }
        else if ("cartoon".equals(faceType)) {
            sb.append("这是个卡通人物");
        }
        this.description = sb.toString();
    }
}
