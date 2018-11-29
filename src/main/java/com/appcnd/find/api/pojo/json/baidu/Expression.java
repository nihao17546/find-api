package com.appcnd.find.api.pojo.json.baidu;

import lombok.Data;

/**
 * @author nihao 2018/11/28
 */
@Data
public class Expression {
    private String type;// none:不笑；smile:微笑；laugh:大笑
    private Double probability;
}
