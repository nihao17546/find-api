package com.appcnd.find.api.service.impl;

import com.appcnd.find.api.dao.IMessageDAO;
import com.appcnd.find.api.dao.IUserDAO;
import com.appcnd.find.api.pojo.po.MessagePO;
import com.appcnd.find.api.pojo.po.UserPO;
import com.appcnd.find.api.pojo.vo.MessageVO;
import com.appcnd.find.api.service.IMessageService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author nihao
 * @create 2018/12/1
 **/
@Service
public class MessageServiceImpl implements IMessageService {

    @Resource
    private IMessageDAO messageDAO;
    @Resource
    private IUserDAO userDAO;

    @Override
    public List<MessageVO> list(Long uid, Integer offset, Integer limit) {
        List<MessagePO> poList = messageDAO.select(uid, offset, limit);
        List<Long> uids = new ArrayList<>();
        List<MessageVO> voList = poList.stream().map(po -> {
            MessageVO vo = new MessageVO();
            BeanUtils.copyProperties(po, vo);
            if (vo.getFromUid() != null && vo.getFromUid() != 0) {
                if (!uids.contains(vo.getFromUid())) {
                    uids.add(vo.getFromUid());
                }
            }
            return vo;
        }).collect(Collectors.toList());
        Map<Long,UserPO> userPOMap = null;
        if (!uids.isEmpty()) {
            userPOMap = userDAO.selectByIds(uids);
        }
        for (MessageVO messageVO : voList) {
            if (userPOMap != null) {
                UserPO userPO = userPOMap.get(messageVO.getFromUid());
                if (userPO != null) {
                    messageVO.setHeadPic(userPO.getHeadPic());
                }
            }
            if (messageVO.getHeadPic() == null) {
                messageVO.setHeadPic("../../images/icon64_appwx_logo.png");
            }
        }
        return voList;
    }
}
