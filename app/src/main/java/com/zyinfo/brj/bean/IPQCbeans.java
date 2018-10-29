package com.zyinfo.brj.bean;

import java.io.Serializable;

/**
 * Created by Zwei  on 2018/5/24.
 * E-Mail Address：592296083@qq.com
 */

public class IPQCbeans implements Serializable {


    /**
     * id : 47a89efddd574dff8183e07cff16140a
     * office : 2817水管站
     * tm : 2018-04-24 00:00:00.0
     * can : 测试
     * classification : 无
     * part : 测试一部
     * description :
     * grade : 无
     * mgr : 测试人员
     * tel : 23421
     * label : 水库
     */

    private String id;
    private String office;
    private String tm;
    private String can;
    private String classification;
    private String part;
    private String description;
    private String grade;
    private String mgr;
    private String tel;
    private String label;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getTm() {
        return tm;
    }

    public void setTm(String tm) {
        this.tm = tm;
    }

    public String getCan() {
        return can;
    }

    public void setCan(String can) {
        this.can = can;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getMgr() {
        return mgr;
    }

    public void setMgr(String mgr) {
        this.mgr = mgr;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
