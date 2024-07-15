package com.bonc.plugin.agent.entity.reception;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/25
 */
public class SessionReceptionEntity {

    //会话id
    private String sessionId;
    //组织接待下的三种类型-新增接口
    /**
     * orderCar
     * business
     * orderRoom
     */
    private List<Map<String,String>> receptionTypeList;

    /**
     * 修改接口，传入具体提交的类型
     */
    private Map<String,String> receptionType;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<Map<String, String>> getReceptionTypeList() {
        return receptionTypeList;
    }

    public void setReceptionTypeList(List<Map<String, String>> receptionTypeList) {
        this.receptionTypeList = receptionTypeList;
    }

    public Map<String, String> getReceptionType() {
        return receptionType;
    }

    public void setReceptionType(Map<String, String> receptionType) {
        this.receptionType = receptionType;
    }

    @Override
    public String toString() {
        return "SessionReceptionEntity{" +
                "sessionId='" + sessionId + '\'' +
                ", receptionTypeList=" + receptionTypeList +
                ", receptionType='" + receptionType + '\'' +
                '}';
    }
}
