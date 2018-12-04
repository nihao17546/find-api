package com.appcnd.find.api.pojo.po;

import lombok.Data;

import java.util.Date;

/**
 * @author nihao
 * @create 2018/12/2
 **/
@Data
public class FaceResultPO {
    private Long id;
    private Long uid;
    private String json;
    private Long picId;
    private Date createdAt;
    private Boolean flag;
}
