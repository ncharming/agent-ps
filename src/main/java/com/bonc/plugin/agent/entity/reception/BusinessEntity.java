package com.bonc.plugin.agent.entity.reception;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/21
 */
public class BusinessEntity {
    // 对外招待类型  外事招待  、商务招待、其他公务
    private String foreignType;
    //业务招待日期
    private String datatime;
    //事由
    private String matter;
    //业务招待用途
    private String behoof;
    //业务招待地点
    private String place;

    //业务招待类型   对内  对外
    private String behoofType;
    private List<Receive> receive;

    // 是否需要电子餐券
    private String iscanq;
    // 招待对象
    private String Guests;
    //业务招待时间
    private String time;
    //是否领导参加
    private String isLeader;

    //招待详情
    class Receive {
        //招待标准
        private String standard;
        //我方人数
        private int meNum = 0;
        //合计数量
        private int num = 0;
        //对方人数
        private int youNum = 0;

        public String getStandard() {
            return standard;
        }

        public void setStandard(String standard) {
            this.standard = standard;
        }

        public int getMeNum() {
            return meNum;
        }

        public void setMeNum(int meNum) {
            this.meNum = meNum;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getYouNum() {
            return youNum;
        }

        public void setYouNum(int youNum) {
            this.youNum = youNum;
        }
    }


    public String getForeignType() {
        return foreignType;
    }

    public void setForeignType(String foreignType) {
        this.foreignType = foreignType;
    }

    public String getDatatime() {
        return datatime;
    }

    public void setDatatime(String datatime) {
        this.datatime = datatime;
    }

    public String getMatter() {
        return matter;
    }

    public void setMatter(String matter) {
        this.matter = matter;
    }

    public String getBehoof() {
        return behoof;
    }

    public void setBehoof(String behoof) {
        this.behoof = behoof;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getBehoofType() {
        return behoofType;
    }

    public void setBehoofType(String behoofType) {
        this.behoofType = behoofType;
    }

    public List<Receive> getReceive() {
        return receive;
    }

    public void setReceive(List<Receive> receive) {
        this.receive = receive;
    }

    public String getIscanq() {
        return iscanq;
    }

    public void setIscanq(String iscanq) {
        this.iscanq = iscanq;
    }

    @JSONField(name = "Guests")
    public String getGuests() {
        return Guests;
    }

    public void setGuests(String guests) {
        Guests = guests;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIsLeader() {
        return isLeader;
    }

    public void setIsLeader(String isLeader) {
        this.isLeader = isLeader;
    }


    /**
     * 下面的传空字符串就行
     */
    private boolean issanji = false;
    private boolean iserjizheng = false;
    private boolean Level3 = false;
    private boolean iserjifu = false;
    private String sponsor = "";
    private String deptou = "";
    private String reimburName = "";
    private String costId = "";
    private String total = "";
    private String balance = "";
    private String headline = "";
    private String budget = "";
    private String meNum = "";
    private String item = "";
    private String costCenter = "";
    private String youNum = "";
    //对外招待类型
    private String internalType = "";
    private String affirm = "";
    private String reimburHrid = "";
    private String party = "";

    private List file = new ArrayList<>();
    private CostCentersel costCentersel;

    class CostCentersel {
        private String amount = "";
        private String item = "";
        private String flfxid = "";
        private String flfxname = "";
        private String surplusamount = "";
        private String remarks2 = "";
        private String userid = "";
        private String orgid = "";
        private String live = "";
        private String remarks = "";

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public String getFlfxid() {
            return flfxid;
        }

        public void setFlfxid(String flfxid) {
            this.flfxid = flfxid;
        }

        public String getFlfxname() {
            return flfxname;
        }

        public void setFlfxname(String flfxname) {
            this.flfxname = flfxname;
        }

        public String getSurplusamount() {
            return surplusamount;
        }

        public void setSurplusamount(String surplusamount) {
            this.surplusamount = surplusamount;
        }

        public String getRemarks2() {
            return remarks2;
        }

        public void setRemarks2(String remarks2) {
            this.remarks2 = remarks2;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getOrgid() {
            return orgid;
        }

        public void setOrgid(String orgid) {
            this.orgid = orgid;
        }

        public String getLive() {
            return live;
        }

        public void setLive(String live) {
            this.live = live;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }

    public boolean isIssanji() {
        return issanji;
    }

    public void setIssanji(boolean issanji) {
        this.issanji = issanji;
    }

    public boolean isIserjizheng() {
        return iserjizheng;
    }

    public void setIserjizheng(boolean iserjizheng) {
        this.iserjizheng = iserjizheng;
    }

    public boolean isLevel3() {
        return Level3;
    }

    public void setLevel3(boolean level3) {
        Level3 = level3;
    }

    public boolean isIserjifu() {
        return iserjifu;
    }

    public void setIserjifu(boolean iserjifu) {
        this.iserjifu = iserjifu;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getDeptou() {
        return deptou;
    }

    public void setDeptou(String deptou) {
        this.deptou = deptou;
    }

    public String getReimburName() {
        return reimburName;
    }

    public void setReimburName(String reimburName) {
        this.reimburName = reimburName;
    }

    public String getCostId() {
        return costId;
    }

    public void setCostId(String costId) {
        this.costId = costId;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getMeNum() {
        return meNum;
    }

    public void setMeNum(String meNum) {
        this.meNum = meNum;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getYouNum() {
        return youNum;
    }

    public void setYouNum(String youNum) {
        this.youNum = youNum;
    }

    public String getInternalType() {
        return internalType;
    }

    public void setInternalType(String internalType) {
        this.internalType = internalType;
    }

    public String getAffirm() {
        return affirm;
    }

    public void setAffirm(String affirm) {
        this.affirm = affirm;
    }

    public String getReimburHrid() {
        return reimburHrid;
    }

    public void setReimburHrid(String reimburHrid) {
        this.reimburHrid = reimburHrid;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public List getFile() {
        return file;
    }

    public void setFile(List file) {
        this.file = file;
    }

    public CostCentersel getCostCentersel() {
        return costCentersel;
    }

    public void setCostCentersel(CostCentersel costCentersel) {
        this.costCentersel = costCentersel;
    }
}
