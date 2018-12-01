package com.appcnd.find.api.service;

import com.appcnd.find.api.pojo.vo.MessageVO;

import java.util.List;

/**
 * @author nihao
 * @create 2018/12/1
 **/
public interface IMessageService {
    List<MessageVO> list(Long uid, Integer offset, Integer limit);
}
