package com.appcnd.find.api.dao;

import com.appcnd.find.api.pojo.dto.FavoImageDTO;
import com.appcnd.find.api.pojo.po.ImagePO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by nihao on 17/6/15.
 */
public interface ImageDAO {
    List<ImagePO> selectRandom(@Param("limit") Integer limit);
    int insertError(@Param("imageId") Long imageId, @Param("errMsg") String errMsg);
    List<FavoImageDTO> selectOwnFavo(@Param("uid") Long uid, RowBounds rowBounds);
    long selectOwnCount(@Param("uid") Long uid);
    int insertPic(ImagePO imagePO);
    ImagePO selectById(@Param("id") Long id);
}
