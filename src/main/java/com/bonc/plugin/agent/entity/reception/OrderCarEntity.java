package com.bonc.plugin.agent.entity.reception;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/21
 */
public class OrderCarEntity {

    private String deptou = "";
    private List<Star> star;
    private List<Malldata> mallData;
    private String filingDate = "";
    private String telephone = "";
    private String deptname = "";

    public List<OrderCarEntity.Officecoordination> getOfficecoordination() {
        return Officecoordination;
    }

    public void setOfficecoordination(List<OrderCarEntity.Officecoordination> officecoordination) {
        Officecoordination = officecoordination;
    }


    private List<Officecoordination> Officecoordination = new ArrayList<>();
    private String descr = "";
    private String somthing = "";
    private String departmentdddd = "";
    private List evaluate = new ArrayList<>();
    private String email = "";
    private List filelist = new ArrayList<>();
    private String applicationsectors = "";
    private String tertiarydept = "";


    public String getDeptou() {
        return deptou;
    }

    public void setDeptou(String deptou) {
        this.deptou = deptou;
    }

    public List<Star> getStar() {
        return star;
    }

    public void setStar(List<Star> star) {
        this.star = star;
    }

    public List<Malldata> getMallData() {
        return mallData;
    }

    public void setMallData(List<Malldata> mallData) {
        this.mallData = mallData;
    }

    public String getFilingDate() {
        return filingDate;
    }

    public void setFilingDate(String filingDate) {
        this.filingDate = filingDate;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }



    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getSomthing() {
        return somthing;
    }

    public void setSomthing(String somthing) {
        this.somthing = somthing;
    }

    public String getDepartmentdddd() {
        return departmentdddd;
    }

    public void setDepartmentdddd(String departmentdddd) {
        this.departmentdddd = departmentdddd;
    }

    public List<String> getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(List<String> evaluate) {
        this.evaluate = evaluate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getFilelist() {
        return filelist;
    }

    public void setFilelist(List<String> filelist) {
        this.filelist = filelist;
    }

    public String getApplicationsectors() {
        return applicationsectors;
    }

    public void setApplicationsectors(String applicationsectors) {
        this.applicationsectors = applicationsectors;
    }

    public String getTertiarydept() {
        return tertiarydept;
    }

    public void setTertiarydept(String tertiarydept) {
        this.tertiarydept = tertiarydept;
    }

    public class Star {
        private boolean flag;
        private String backgroundposition;

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public String getBackgroundposition() {
            return backgroundposition;
        }

        public void setBackgroundposition(String backgroundposition) {
            this.backgroundposition = backgroundposition;
        }
    }

    class Malldata {

        private int sectors;
        private String amount;
        private String scrap;
        private String begindata;
        private String cause;
        private String begin;
        private String overdata;
        private String username;

        public int getSectors() {
            return sectors;
        }

        public void setSectors(int sectors) {
            this.sectors = sectors;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getScrap() {
            return scrap;
        }

        public void setScrap(String scrap) {
            this.scrap = scrap;
        }

        public String getBegindata() {
            return begindata;
        }

        public void setBegindata(String begindata) {
            this.begindata = begindata;
        }

        public String getCause() {
            return cause;
        }

        public void setCause(String cause) {
            this.cause = cause;
        }

        public String getBegin() {
            return begin;
        }

        public void setBegin(String begin) {
            this.begin = begin;
        }

        public String getOverdata() {
            return overdata;
        }

        public void setOverdata(String overdata) {
            this.overdata = overdata;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    class Officecoordination {
        private String carnumber;
        private String carname;
        private String carphone;

        public String getCarnumber() {
            return carnumber;
        }

        public void setCarnumber(String carnumber) {
            this.carnumber = carnumber;
        }

        public String getCarname() {
            return carname;
        }

        public void setCarname(String carname) {
            this.carname = carname;
        }

        public String getCarphone() {
            return carphone;
        }

        public void setCarphone(String carphone) {
            this.carphone = carphone;
        }
    }
}
