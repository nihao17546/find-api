package com.appcnd.find.api.dao;

import com.appcnd.find.api.pojo.po.UserFavoPO;
import com.appcnd.find.api.pojo.po.UserPO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by nihao on 17/6/11.
 */
public interface IUserDAO {
    int insert(UserPO userPO);
    UserPO selectByUserId(@Param("userId") String userId);
    UserPO selectByUserIdAndPassword(@Param("userId") String userId, @Param("password") String password);
    UserPO selectByUnionId(@Param("unionId") String unionId);
    int insertFavo(UserFavoPO userFavoPO);
    int deleteFavo(@Param("uid") Long uid, @Param("picId") Long picId);
    int insertSuggestion(@Param("uid") Long uid, @Param("content") String content);
    int updateByUnionId(UserPO userPO);
    UserPO selectById(@Param("id") Long id);
    @MapKey("id")
    Map<Long,UserPO> selectByIds(@Param("ids") List<Long> ids);
}
