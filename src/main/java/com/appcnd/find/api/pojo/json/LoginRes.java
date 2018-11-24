package com.appcnd.find.api.pojo.json;

import lombok.Data;

/**
 * @author nihao
 * @create 2018/11/24
 **/
@Data
public class LoginRes {
    private String code;
    private String encryptedData;
    private String errMsg;
    private String iv;
    private UserInfo userInfo;
    private String signature;
    private String rawData;
}
