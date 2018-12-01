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
                    sb.append("这个大概").append(age).append("岁的小男孩大");
                }
                else if (age <= 16){
                    sb.append("这个大概").append(age).append("岁的小伙子");
                }
                else if (age <= 30){
                    sb.append("这位大概").append(age).append("岁的帅哥");
                }
                else if (age <= 55){
                    sb.append("这位大概").append(age).append("岁的大叔");
                }
                else {
                    sb.append("这位大概").append(age).append("岁老爷爷");
                }
            }
            else if ("female".equals(gender)) {
                if (age <= 10) {
                    sb.append("这个大概").append(age).append("岁的小姑娘");
                }
                else if (age <= 16){
                    sb.append("这个大概").append(age).append("岁的小女孩");
                }
                else if (age <= 30){
                    sb.append("这位大概").append(age).append("岁的美女");
                }
                else if (age <= 55){
                    sb.append("这位大概").append(age).append("岁的大婶");
                }
                else {
                    sb.append("这位大概").append(age).append("岁的老奶奶");
                }
            }

            if ("yellow".equals(race)) {
                sb.append("，可能来自亚洲");
            }
            else if ("white".equals(race)) {
                sb.append("，可能来自欧洲或者北美地区");
            }
            else if ("black".equals(race)) {
                sb.append("，可能来自非洲");
            }
            else if ("arabs".equals(race)) {
                sb.append("，应该来自阿拉伯地区");
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
