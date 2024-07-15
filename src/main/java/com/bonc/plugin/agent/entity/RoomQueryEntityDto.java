package com.bonc.plugin.agent.entity;


/**
 * @description:
 * 查询会议室实体
 * @author：nihongyu
 * @date: 2024/6/6
 */
public class RoomQueryEntityDto {

    /**
     *  当天被占用的时间 datetime是空则当天没有占用
     */
    private String datetime;
    //开始时间
    private String starttime;
    //结束时间
    private String terminaltime;
    //会议室id
    private String roomid;
    //会议室名称
    private String rooms;

    private String remarks1;
    //楼层
    private String floor;
    private int floorNum;
    private String id;

    @Override
    public String toString() {
        return "RoomQueryEntityDto{" +
                "datetime='" + datetime + '\'' +
                ", starttime='" + starttime + '\'' +
                ", terminaltime='" + terminaltime + '\'' +
                ", roomid='" + roomid + '\'' +
                ", rooms='" + rooms + '\'' +
                ", remarks1='" + remarks1 + '\'' +
                ", floor='" + floor + '\'' +
                ", floorNum=" + floorNum +
                ", id='" + id + '\'' +
                '}';
    }

    public int getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }

    public String getRemarks1() {
        return remarks1;
    }

    public void setRemarks1(String remarks1) {
        this.remarks1 = remarks1;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getTerminaltime() {
        return terminaltime;
    }

    public void setTerminaltime(String terminaltime) {
        this.terminaltime = terminaltime;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getRooms() {
        return rooms;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

}
