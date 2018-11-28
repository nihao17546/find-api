package com.appcnd.find.api.pojo.json.baidu;

import lombok.Data;

/**
 * @author nihao 2018/11/28
 */
@Data
public class Angel {
    private Double yaw;// 三维旋转之左右旋转角[-90(左), 90(右)]
    private Double pitch;// 三维旋转之俯仰角度[-90(上), 90(下)]
    private Double roll;// 平面内旋转角[-180(逆时针), 180(顺时针)]
}
