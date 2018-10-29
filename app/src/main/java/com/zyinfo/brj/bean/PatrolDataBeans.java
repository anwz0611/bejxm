package com.zyinfo.brj.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Zwei  on 2018/6/27.
 * E-Mail Address：592296083@qq.com
 */

public class PatrolDataBeans {
    @SerializedName("cantype")

    private List<CantypeBean> cantype;
    @SerializedName("yhdj")

    private List<YhdjBean> yhdj;
    @SerializedName("manageUnit")

    private List<ManageUnitBean> manageUnit;

    public List<CantypeBean> getCantype() {
        return cantype;
    }

    public void setCantype(List<CantypeBean> cantype) {
        this.cantype = cantype;
    }

    public List<YhdjBean> getYhdj() {
        return yhdj;
    }

    public void setYhdj(List<YhdjBean> yhdj) {
        this.yhdj = yhdj;
    }

    public List<ManageUnitBean> getManageUnit() {
        return manageUnit;
    }

    public void setManageUnit(List<ManageUnitBean> manageUnit) {
        this.manageUnit = manageUnit;
    }

    public static class CantypeBean {
        /**
         * value : 1
         * label : 水库
         */

        private String value;
        private String label;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    public static class YhdjBean {
        /**
         * value : 2
         * label : 严重隐患
         */

        private String value;
        private String label;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    public static class ManageUnitBean {
        /**
         * id : 441717099e5846149f7544e98c48e684
         * name : 冲乎尔镇水管站
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
