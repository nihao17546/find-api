package com.appcnd.find.api.pojo.json.baidu;

import lombok.Data;

import java.util.List;

/**
 * @author nihao 2018/11/28
 */
@Data
public class Baidu {
    private Integer face_num;
    private List<Face> face_list;
}
