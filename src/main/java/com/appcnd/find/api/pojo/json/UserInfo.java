package com.appcnd.find.api.pojo.json;

import lombok.Data;

/**
 * @author nihao 2018/11/23
 */
@Data
public class UserInfo {
    private String nickName;
    private Integer gender;
    private String city;
    private String province;
    private String country;
    private String avatarUrl;
    private String unionId;
}
