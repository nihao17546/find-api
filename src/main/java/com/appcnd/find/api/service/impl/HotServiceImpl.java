package com.appcnd.find.api.service.impl;

import com.appcnd.find.api.dao.IHotDAO;
import com.appcnd.find.api.pojo.po.HotKeywordPO;
import com.appcnd.find.api.service.IHotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nihao
 * @create 2018/11/24
 **/
@Service
public class HotServiceImpl implements IHotService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IHotDAO hotDAO;

    @Override
    public List<String> getHots(Integer sum) {
        List<HotKeywordPO> hotKeywordPOList = hotDAO.select(sum);
        List<String> list = hotKeywordPOList.stream().map(po -> {
            return po.getKeyword();
        }).collect(Collectors.toList());
        return list;
    }
}
