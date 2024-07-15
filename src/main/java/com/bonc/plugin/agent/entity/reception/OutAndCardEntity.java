package com.bonc.plugin.agent.entity.reception;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/7/2
 */
public class OutAndCardEntity {
    private List<Detail> list;
    private List fileList=new ArrayList<>();
    //标题
    private String title="";
    //备注说明
    private String descr="";
    //职位
    private String userPosition="";


    class Detail {
        //异常类型
        private String type;
        //开始时间
        private String startTime;
        //时间类型
        private String day;
        //是否跨天
        private String step;
        //结束时间
        private String endTime;
        //时间类型
        private String day1;
        //异常原因
        private String department;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getStep() {
            return step;
        }

        public void setStep(String step) {
            this.step = step;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getDay1() {
            return day1;
        }

        public void setDay1(String day1) {
            this.day1 = day1;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }
    }


    public List<Detail> getList() {
        return list;
    }

    public void setList(List<Detail> list) {
        this.list = list;
    }

    public List getFileList() {
        return fileList;
    }

    public void setFileList(List fileList) {
        this.fileList = fileList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(String userPosition) {
        this.userPosition = userPosition;
    }
}

