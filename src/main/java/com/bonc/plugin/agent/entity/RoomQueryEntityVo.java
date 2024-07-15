package com.bonc.plugin.agent.entity;


import javax.validation.constraints.NotBlank;

/**
 * @description:
 * 查询会议室实体
 * @author：nihongyu
 * @date: 2024/6/6
 */
public class RoomQueryEntityVo {

    private String date;
    private String floor;
    //传am代表上午 pm代表下午  可以传时间段 用半角逗号隔开  08:00,10:30 不传代表全天
    private String ampm;
    private String username;

    /**
     * 部门编码，用于查询预定人的推荐楼层
     */
    private String deptNo;

    private String place;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getAmpm() {
        return ampm;
    }

    public void setAmpm(String ampm) {
        this.ampm = ampm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "RoomQueryEntityVo{" +
                "date='" + date + '\'' +
                ", floor='" + floor + '\'' +
                ", ampm='" + ampm + '\'' +
                ", username='" + username + '\'' +
                ", deptNo='" + deptNo + '\'' +
                ", place='" + place + '\'' +
                '}';
    }

}
