package com.bonc.plugin.agent.entity;


import javax.validation.constraints.NotBlank;

/**
 * @description:
 * 预定会议室实体
 * @author：nihongyu
 * @date: 2024/6/6
 */
public class RoomReservationEntityReq {

    //会议名称
    @NotBlank()
    private String meetingname;
    //开始时间
    @NotBlank()
    private String date;
    //结束时间
//    @NotBlank()
    private String ampm;
    //会议室名称
    @NotBlank()
    private String name1;
    @NotBlank()
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "RoomReservationEntityReq{" +
                "meetingname='" + meetingname + '\'' +
                ", date='" + date + '\'' +
                ", ampm='" + ampm + '\'' +
                ", name1='" + name1 + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public String getAmpm() {
        return ampm;
    }

    public void setAmpm(String ampm) {
        this.ampm = ampm;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMeetingname() {
        return meetingname;
    }

    public void setMeetingname(String meetingname) {
        this.meetingname = meetingname;
    }


    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }


}
