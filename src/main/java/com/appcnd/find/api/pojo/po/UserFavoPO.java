package com.appcnd.find.api.pojo.po;

import lombok.Data;

import java.util.Date;

/**
 * Created by nihao on 17/6/15.
 */
@Data
public class UserFavoPO {
    private Long uid;
    private Long picId;
    private Date createdAt;

    public UserFavoPO() {
    }

    public UserFavoPO(Long uid, Long picId) {
        this.uid = uid;
        this.picId = picId;
    }
}
