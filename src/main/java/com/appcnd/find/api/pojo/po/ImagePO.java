package com.appcnd.find.api.pojo.po;

import lombok.Data;

import java.util.Date;

/**
 * Created by nihao on 17/4/14.
 */
@Data
public class ImagePO {
    private Long id;
    private String title;
    private String compressSrc;
    private String src;
    private Integer width;
    private Integer height;
    private Date createdAt;
    private Boolean flag;
    private Long uid;
    private Date dihTime;
}
