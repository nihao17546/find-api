package com.appcnd.find.api.dao;

import com.appcnd.find.api.pojo.po.HotKeywordPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by nihao on 17/6/29.
 */
public interface IHotDAO {
    List<HotKeywordPO> select(@Param("limit") Integer limit);
}
