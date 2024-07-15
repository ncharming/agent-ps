package com.bonc.plugin.agent.service;

import com.bonc.plugin.agent.entity.RoomQueryEntityVo;
import com.bonc.plugin.agent.entity.RoomReservationEntityReq;
import com.bonc.plugin.agent.util.ResultObject;

import java.util.Map;


/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/6
 */
public interface IAgentRoomService {
    ResultObject roomlist(RoomQueryEntityVo roomQueryEntityVo);
    ResultObject roomReservation(RoomReservationEntityReq roomReservationEntityReq);
    ResultObject reservationList(RoomQueryEntityVo roomQueryEntityVo);
    ResultObject floorlist();
    ResultObject webhook(Map<String,String> map);
    ResultObject decrypt(Map<String,String> params);

}
