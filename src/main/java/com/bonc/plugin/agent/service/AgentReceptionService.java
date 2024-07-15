package com.bonc.plugin.agent.service;

import com.bonc.plugin.agent.entity.reception.SessionReceptionEntity;
import com.bonc.plugin.agent.util.ResultObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/21
 */
public interface AgentReceptionService {

    ResultObject reception(Map<String,Object> map, HttpServletRequest request);

    ResultObject createSeesion(SessionReceptionEntity map, HttpServletRequest request);
    ResultObject updateSeesion(SessionReceptionEntity map, HttpServletRequest request);
    ResultObject querySession(SessionReceptionEntity map, HttpServletRequest request);
    ResultObject orderCarTime(Map<String,String> params,HttpServletRequest request);
    ResultObject workflow(Map<String,String> params,HttpServletRequest request);
}
