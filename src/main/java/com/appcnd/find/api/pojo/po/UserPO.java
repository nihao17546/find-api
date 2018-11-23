package com.appcnd.find.api.pojo.po;

import lombok.Data;

import java.util.Date;

/**
 * Created by nihao on 17/6/11.
 */
@Data
public class UserPO {
    private Long id;
    private String userId;
    private String password;
    private String nickname;
    private Integer gender;
    private String headPic;
    private String email;
    private String unionId;
    private String country;
    private String city;
    private String province;
    private Date createdAt;
    private Date updatedAt;
}
