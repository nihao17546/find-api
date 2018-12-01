package com.appcnd.find.api.pojo.po;

import lombok.Data;

import java.util.Date;

/**
 * @author nihao
 * @create 2018/12/1
 **/
@Data
public class MessagePO {
    private Long id;
    private Long toUid;
    private Long fromUid;
    private String title;
    private String content;
    private Boolean read;
    private Date createdAt;
}
