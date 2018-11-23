package com.appcnd.find.api.pojo;

import com.appcnd.find.api.pojo.po.UserPO;
import lombok.Data;

/**
 * @author nihao 2018/11/23
 */
@Data
public class UserVO extends UserPO {

    private String token;

}
