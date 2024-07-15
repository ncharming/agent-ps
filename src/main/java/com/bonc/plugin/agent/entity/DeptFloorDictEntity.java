package com.bonc.plugin.agent.entity;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/7
 */
public class DeptFloorDictEntity {
    private String deptNo;
    private String deptName;
    private String floor;
    private int floorNum;

    @Override
    public String toString() {
        return "DeptFloorDictEntity{" +
                "deptNo='" + deptNo + '\'' +
                ", deptName='" + deptName + '\'' +
                ", floor='" + floor + '\'' +
                ", floorNum=" + floorNum +
                '}';
    }

    public int getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }

    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

}
