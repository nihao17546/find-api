package com.appcnd.find.api.pojo.vo;

import com.appcnd.find.api.pojo.po.FaceResultPO;
import lombok.Data;

/**
 * @author nihao
 * @create 2018/12/2
 **/
@Data
public class FaceResultVO extends FaceResultPO {
    private String userName;
    private String headPic;
    private FaceListVO face;
}
