package com.appcnd.find.api.dao;

import com.appcnd.find.api.pojo.po.MessagePO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author nihao
 * @create 2018/12/1
 **/
public interface IMessageDAO {
    int insert(MessagePO messagePO);
    List<MessagePO> select(@Param("toUid") Long toUid, @Param("offset") Integer offset, @Param("limit") Integer limit);
}
