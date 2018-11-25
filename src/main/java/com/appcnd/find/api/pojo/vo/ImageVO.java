package com.appcnd.find.api.pojo.vo;


import com.appcnd.find.api.pojo.po.ImagePO;
import lombok.Data;

import java.util.List;

/**
 * Created by nihao on 17/4/14.
 */
@Data
public class ImageVO extends ImagePO {
    private List<String> tags;
}
