package com.appcnd.find.api.util;

import com.alibaba.fastjson.JSON;
import com.appcnd.find.api.conf.SecretConfig;
import com.appcnd.find.api.exception.FindException;
import com.appcnd.find.api.pojo.vo.FaceVO;
import lombok.Data;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by nihao on 17/9/7.
 */
@Component
public class BaiduUtils {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String grant_type = "client_credentials";
    private final String tokenUrl = "https://aip.baidubce.com/oauth/2.0/token";
    private final String detectUrl = "https://aip.baidubce.com/rest/2.0/face/v1/detect";

    @Autowired
    private SecretConfig secretConfig;

    public String getBaiduToken() throws FindException {
        String result = null;
        CloseableHttpResponse httpResponse = null;
        HttpEntity entity = null;
        try{
            CloseableHttpClient httpClient = HttpClientUtils.getHttpClient();
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addTextBody("grant_type", grant_type)
                    .addTextBody("client_id", secretConfig.getBaiduAppId())
                    .addTextBody("client_secret", secretConfig.getBaiduSecretKey())
                    .build();
            HttpPost httpPost = new HttpPost(tokenUrl);
            httpPost.setEntity(reqEntity);
            HttpClientUtils.config(httpPost);
            httpResponse = httpClient.execute(httpPost);
            entity = httpResponse.getEntity();
            String str = EntityUtils.toString(entity);
            Map<String,String> map = JSON.parseObject(str,Map.class);
            if(map.containsKey("access_token")){
                result = map.get("access_token");
                return result;
            }
            else{
                throw new FindException("未找到access_token,响应数据:" + str);
            }
        }catch (FindException e){
            throw e;
        } catch (ClientProtocolException e) {
            logger.error("获取百度token错误", e);
            throw new FindException("抱歉，服务异常，请稍后再试！");
        } catch (IOException e) {
            logger.error("获取百度token错误", e);
            throw new FindException("抱歉，服务异常，请稍后再试！");
        } finally {
            if(httpResponse != null){
                try {
                    httpResponse.close();
                } catch (Exception e) {
                    logger.error("close CloseableHttpResponse error", e);
                }
            }
            if(entity != null){
                try {
                    EntityUtils.consume(entity);
                } catch (Exception e) {
                    logger.error("close HttpEntity error", e);
                }
            }
        }
    }

    /**
     * 人脸检测
     * @param image
     * @return
     */
    public Detect detect(String image) throws FindException {
        Detect detect = null;
        CloseableHttpResponse httpResponse = null;
        HttpEntity entity = null;
        try{
            CloseableHttpClient httpClient = HttpClientUtils.getHttpClient();
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addTextBody("max_face_num", "1")
                    .addTextBody("face_fields", "age,beauty,expression,faceshape,gender,glasses,race,qualities")
                    .addTextBody("access_token", getBaiduToken())
                    .addTextBody("image", image)
                    .build();
            HttpPost httpPost = new HttpPost(detectUrl);
            httpPost.setEntity(reqEntity);
            HttpClientUtils.config(httpPost);
            httpResponse = httpClient.execute(httpPost);
            entity = httpResponse.getEntity();
            String str = EntityUtils.toString(entity);
            Map<String,Object> map = JSON.parseObject(str);
            if(map.containsKey("error_code")){
                throw new FindException("响应数据:" + str);
            }
            try{
                detect = JSON.parseObject(str, Detect.class);
                return detect;
            }catch (Exception e){
                throw new FindException("响应数据JSON解析错误:" + str);
            }
        }catch (FindException e){
            throw e;
        } catch (ClientProtocolException e) {
            logger.error("百度人脸检测错误", e);
            throw new FindException("抱歉，服务异常，请稍后再试！");
        } catch (IOException e) {
            logger.error("百度人脸检测错误", e);
            throw new FindException("抱歉，服务异常，请稍后再试！");
        } finally {
            if(httpResponse != null){
                try {
                    httpResponse.close();
                } catch (Exception e) {
                    logger.error("close CloseableHttpResponse error", e);
                }
            }
            if(entity != null){
                try {
                    EntityUtils.consume(entity);
                } catch (Exception e) {
                    logger.error("close HttpEntity error", e);
                }
            }
        }
    }

    private static DecimalFormat df = new DecimalFormat("######0");
    public static FaceVO getFace(DetectResult detectResult){
        FaceVO faceVO = new FaceVO();
        if("male".equals(detectResult.getGender())){
            faceVO.setGender("男");
        }
        else{
            faceVO.setGender("女");
        }
        faceVO.setGenderProbability(detectResult.getGender_probability());
        faceVO.setAge(Integer.parseInt(df.format(detectResult.getAge())));
        faceVO.setBeauty(df.format(detectResult.getBeauty()));
        if(detectResult.getExpression() == 0){
            faceVO.setExpression("正常");
        }
        else if(detectResult.getExpression() == 1){
            faceVO.setExpression("微笑");
        }
        else{
            faceVO.setExpression("大笑");
        }
        faceVO.setExpressionProbablity(detectResult.getExpression_probablity());
        faceVO.setGlasses(detectResult.getGlasses() == 0 ? "没戴" : (detectResult.getGlasses() == 2 ? "墨镜" : "戴了的"));
        faceVO.setGlassesProbability(detectResult.getGlasses_probability());
        if("yellow".equals(detectResult.getRace())){
            faceVO.setRace("黄种人");
        }
        else if("white".equals(detectResult.getRace())){
            faceVO.setRace("白人");
        }
        else if("black".equals(detectResult.getRace())){
            faceVO.setRace("黑人");
        }
        else{
            faceVO.setRace("阿拉伯人");
        }
        faceVO.setRaceProbability(detectResult.getRace_probability());
        faceVO.setHuman(detectResult.getQualities().getType().getHuman());
        faceVO.setCartoon(detectResult.getQualities().getType().getCartoon());
        faceVO.setTop(detectResult.getLocation().getTop());
        faceVO.setLeft(detectResult.getLocation().getLeft());
        faceVO.setWidth(detectResult.getLocation().getWidth());
        faceVO.setHeight(detectResult.getLocation().getHeight());
        return faceVO;
    }

    @Data
    public static class Detect{
        private Integer result_num;
        private Long log_id;
        private List<DetectResult> result;
    }

    @Data
    public static class DetectResult{
        private DetectLocation location;//人脸在图片中的位置
        private Double face_probability;//人脸置信度，范围0-1
        private Integer rotation_angle;//人脸框相对于竖直方向的顺时针旋转角，[-180,180]
        private Double yaw;//三维旋转之左右旋转角[-90(左), 90(右)]
        private Double pitch;//三维旋转之俯仰角度[-90(上), 90(下)]
        private Double roll;//平面内旋转角[-180(逆时针), 180(顺时针)]
        private Double age;//年龄
        private Double beauty;//美丑打分，范围0-100，越大表示越美
        private Integer expression;//表情，0，不笑；1，微笑；2，大笑
        private Double expression_probablity;//表情置信度，范围0~1
        private String gender;//male、female
        private Double gender_probability;//性别置信度，范围0~1
        private Integer glasses;//是否带眼镜，0-无眼镜，1-普通眼镜，2-墨镜
        private Double glasses_probability;//眼镜置信度，范围0~1
        private String race;//yellow、white、black、arabs
        private Double race_probability;//人种置信度，范围0~1
        private List<DetectFaceshape> faceshape;//脸型置信度
        private DetectQualities qualities;//人脸质量信息
    }

    @Data
    public static class DetectLocation{
        private Integer left;
        private Integer top;
        private Integer width;
        private Integer height;
    }

    @Data
    public static class DetectFaceshape{
        private String type;//脸型：square/triangle/oval/heart/round
        private Double probability;//置信度：0~1
    }

    @Data
    public static class DetectQualities{
        private DetectQualitiesOcclusion occlusion;//人脸各部分遮挡的概率， [0, 1]
        private DetectQualitiesType type;//真实人脸/卡通人脸置信度
    }

    @Data
    public static class DetectQualitiesType{
        private Double human;//真实人脸置信度，[0, 1]
        private Double cartoon;//卡通人脸置信度，[0, 1]
    }

    @Data
    public static class DetectQualitiesOcclusion{
        private Double left_eye;//左眼
        private Double right_eye;//右眼
        private Double nose;//鼻子
        private Double mouth;//嘴
        private Double left_cheek;//左脸颊
        private Double right_cheek;//右脸颊
        private Double chin;//下巴
    }
}
