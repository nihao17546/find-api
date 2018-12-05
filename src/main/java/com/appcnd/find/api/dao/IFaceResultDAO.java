package com.appcnd.find.api.dao;

import com.appcnd.find.api.pojo.po.FaceResultPO;
import org.apache.ibatis.annotations.Param;

/**
 * @author nihao
 * @create 2018/12/2
 **/
public interface IFaceResultDAO {
    int insert(FaceResultPO faceResultPO);
    FaceResultPO selectById(@Param("id") Long id);
    int updateFaceUrl(@Param("id") Long id, @Param("faceUrl") String faceUrl);
}
