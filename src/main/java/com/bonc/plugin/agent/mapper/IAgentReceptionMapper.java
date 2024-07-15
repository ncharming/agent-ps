package com.bonc.plugin.agent.mapper;

import com.bonc.plugin.agent.entity.reception.SessionReceptionEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/25
 */
@Mapper
public interface IAgentReceptionMapper {
    void insertSession(List<Map<String,String>> map);
    List<Map<String,String>> selectSession(Map<String,String> map);
    void updateSession(Map<String,String> map);

}
