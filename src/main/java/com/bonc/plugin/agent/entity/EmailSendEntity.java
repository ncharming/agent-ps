package com.bonc.plugin.agent.entity;

import java.util.List;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/11
 */
public class EmailSendEntity {

    //主键id
    private Integer id;


    //发件人
    /**
     * 前端传一个 sendDoc 后台拼接
     */
    private String sendDoc;
    //收件人
    private List<String> receivingDoc;

    //会议时间
    private String meetTime;

    //召集部门
    private String meetDesc;

    //会议地点
    private String meetPlace;
    //会议名称
    private String meetName;

    //会议议题
    private String meetTopic;

    //出席对象
    private String meetObject;

    //remark
    private String remark;

    //用于查询
    private String emailId;


    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public List<String> getReceivingDoc() {
        return receivingDoc;
    }

    public void setReceivingDoc(List<String> receivingDoc) {
        this.receivingDoc = receivingDoc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSendDoc() {
        return sendDoc;
    }

    public void setSendDoc(String sendDoc) {
        this.sendDoc = sendDoc;
    }


    public String getMeetTime() {
        return meetTime;
    }

    public void setMeetTime(String meetTime) {
        this.meetTime = meetTime;
    }

    public String getMeetDesc() {
        return meetDesc;
    }

    public void setMeetDesc(String meetDesc) {
        this.meetDesc = meetDesc;
    }

    public String getMeetPlace() {
        return meetPlace;
    }

    public void setMeetPlace(String meetPlace) {
        this.meetPlace = meetPlace;
    }

    public String getMeetName() {
        return meetName;
    }

    public void setMeetName(String meetName) {
        this.meetName = meetName;
    }

    public String getMeetTopic() {
        return meetTopic;
    }

    public void setMeetTopic(String meetTopic) {
        this.meetTopic = meetTopic;
    }

    public String getMeetObject() {
        return meetObject;
    }

    public void setMeetObject(String meetObject) {
        this.meetObject = meetObject;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "EmailSendEntity{" +
                "id=" + id +
                ", sendDoc='" + sendDoc + '\'' +
                ", receivingDoc=" + receivingDoc +
                ", meetTime='" + meetTime + '\'' +
                ", meetDesc='" + meetDesc + '\'' +
                ", meetPlace='" + meetPlace + '\'' +
                ", meetName='" + meetName + '\'' +
                ", meetTopic='" + meetTopic + '\'' +
                ", meetObject='" + meetObject + '\'' +
                ", remark='" + remark + '\'' +
                ", emailId='" + emailId + '\'' +
                '}';
    }
}
