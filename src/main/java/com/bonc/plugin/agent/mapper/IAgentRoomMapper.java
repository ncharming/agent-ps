package com.bonc.plugin.agent.mapper;

import com.bonc.plugin.agent.entity.DeptFloorDictEntity;
import com.bonc.plugin.agent.entity.RoomReservationEntityReq;
import com.bonc.plugin.agent.entity.RoomReservationEntityVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/7
 */


@Mapper
public interface IAgentRoomMapper {

    DeptFloorDictEntity selectDict(String deptNo);
    List<RoomReservationEntityReq> selectReservationList(String loginId, String date);

    void insertlog(RoomReservationEntityVo roomReservationEntityReq);

}
