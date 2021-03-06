package com.appcnd.find.api.pojo.json.baidu;

import lombok.Data;

import java.util.List;

/**
 * @author nihao 2018/11/28
 */
@Data
public class BaiduResult {
    private Integer face_num;
    private List<Face> face_list;

    public BaiduResult() {
        this.face_num = 0;
    }
}
