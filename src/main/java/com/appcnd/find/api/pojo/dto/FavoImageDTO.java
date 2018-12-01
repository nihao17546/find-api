package com.appcnd.find.api.pojo.dto;

import com.appcnd.find.api.pojo.po.ImagePO;
import lombok.Data;

import java.util.Date;

/**
 * @author nihao
 * @create 2018/12/1
 **/
@Data
public class FavoImageDTO extends ImagePO {
    private Date favoDate;
}
