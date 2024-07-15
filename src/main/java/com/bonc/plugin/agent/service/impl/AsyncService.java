package com.bonc.plugin.agent.service.impl;

import com.bonc.plugin.agent.entity.EmailSendEntity;
import com.bonc.plugin.agent.entity.RoomReservationEntityReq;
import com.bonc.plugin.agent.entity.RoomReservationEntityVo;
import com.bonc.plugin.agent.mapper.IAgentEmailMapper;
import com.bonc.plugin.agent.mapper.IAgentRoomMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/7
 */
@Component
public class AsyncService {

    private Logger logger = LoggerFactory.getLogger(AsyncService.class);

    @Autowired
    private IAgentRoomMapper iAgentRoomMapper;

    @Autowired
    private IAgentEmailMapper iAgentEmailMapper;

    @Async
    public void insertlog(RoomReservationEntityVo roomReservationEntityReq) {
        logger.info("预定会议室信息日志插入：{}", roomReservationEntityReq);
        iAgentRoomMapper.insertlog(roomReservationEntityReq);
    }


    @Async
    public void insertEmaillog(EmailSendEntity emailSendEntity) {
//        logger.info("发送邮件信息日志入参：{}", emailSendEntity);
        iAgentEmailMapper.insertEmailLog(emailSendEntity);
    }



}
