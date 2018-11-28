package com.appcnd.find.api.pojo.json.baidu;

import lombok.Data;

import java.util.List;

/**
 * @author nihao 2018/11/28
 */
@Data
public class Face {
    private String face_token;// 人脸图片的唯一标识
    private Location location;// 人脸在图片中的位置
    private Double face_probability;// 人脸置信度，范围【0~1】，代表这是一张人脸的概率，0最小、1最大
    private Angel angel;// 人脸旋转角度参数
    private Double age;// 年龄 ，当face_field包含age时返回
    private Double beauty;// 美丑打分，范围0-100，越大表示越美。当face_fields包含beauty时返回
    private Expression expression;// 表情，当 face_field包含expression时返回
    private FaceShape face_shape;// 脸型，当face_field包含faceshape时返回
    private Gender gender;// 性别，face_field包含gender时返回
    private Glasses glasses;// 是否带眼镜，face_field包含glasses时返回
    private Race race;// 人种 face_field包含race时返回
    private FaceType face_type;// 真实人脸/卡通人脸 face_field包含face_type时返回
    private List<Landmark> landmark;// 4个关键点位置，左眼中心、右眼中心、鼻尖、嘴中心。face_field包含landmark时返回
    private List<Landmark> landmark72;// 72个特征点位置 face_field包含landmark时返回
    private Quality quality;// 人脸质量信息。face_field包含quality时返回
    private String parsing_info;// 人脸分层结果 结果数据是使用gzip压缩后再base64编码 使用前需base64解码后再解压缩 原数据格式为string 形如0,0,0,0,0,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,…
}
