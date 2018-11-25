package com.appcnd.find.api.dao;

import com.appcnd.find.api.pojo.po.Image2TagPO;
import com.appcnd.find.api.pojo.po.ImagePO;
import com.appcnd.find.api.pojo.po.TagPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author nihao
 * @create 2018/11/25
 **/
public interface IPhotoDAO {
    List<ImagePO> selectOldImage(@Param("sId") Long sId,
                                 @Param("eId") Long eId);
    List<Image2TagPO> selectOldImage2TagByImageIds(@Param("imageIds") List<Long> imageIds);
    List<TagPO> selectOldTagByIds(@Param("ids") List<Long> ids);
    int insertNewImage(@Param("list") List<ImagePO> list);
    int insertNewImage2Tag(@Param("list") List<Image2TagPO> list);
    int insertNewTag(@Param("list") List<TagPO> list);
    List<Map<String,Object>> selectIntoSolr(@Param("sId") Long sId,
                                            @Param("eId") Long eId);
}
