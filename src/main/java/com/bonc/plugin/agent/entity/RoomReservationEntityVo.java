package com.bonc.plugin.agent.entity;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/12
 */
public class RoomReservationEntityVo {

    //会议名称

    private String meetingname;
    //开始时间

    private String startTime;
    //结束时间

    private String terminalTime;
    //会议室名称

    private String name1;
    //oa账号

    private String userid;
    //用户部门

    private String sqdeptname;
    //电话

    private String telephone;

    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "RoomReservationEntityVo{" +
                "meetingname='" + meetingname + '\'' +
                ", startTime='" + startTime + '\'' +
                ", terminalTime='" + terminalTime + '\'' +
                ", name1='" + name1 + '\'' +
                ", userid='" + userid + '\'' +
                ", sqdeptname='" + sqdeptname + '\'' +
                ", telephone='" + telephone + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String getMeetingname() {
        return meetingname;
    }

    public void setMeetingname(String meetingname) {
        this.meetingname = meetingname;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTerminalTime() {
        return terminalTime;
    }

    public void setTerminalTime(String terminalTime) {
        this.terminalTime = terminalTime;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSqdeptname() {
        return sqdeptname;
    }

    public void setSqdeptname(String sqdeptname) {
        this.sqdeptname = sqdeptname;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

}

