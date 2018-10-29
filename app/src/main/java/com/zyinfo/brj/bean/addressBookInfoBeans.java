package com.zyinfo.brj.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/2.
 */

public class addressBookInfoBeans  implements Serializable{


    /**
     * dwmc : c单位
     * dwfl : null
     * office : null
     * dwdh : null
     * dwfax : null
     * dwlxr : null
     * dwlxrdh : null
     * dwlxrzd : null
     * remarks : null
     * waterBookDetailList : [{"dwmc":"c单位","dwfl":"23","office":"1","dwdh":"","dwfax":"","dwlxr":"","dwlxrdh":"","dwlxrzd":"","remarks":"","waterBookDetailList":null}]
     */
    private static final long serialVersionUID = 1L;
    private String dwmc;
    private Object dwfl;
    private Object office;
    private Object dwdh;
    private Object dwfax;
    private Object dwlxr;
    private Object dwlxrdh;
    private Object dwlxrzd;
    private Object remarks;
    private List<WaterBookDetailListBean> waterBookDetailList;

    public String getDwmc() {
        return dwmc;
    }

    public void setDwmc(String dwmc) {
        this.dwmc = dwmc;
    }

    public Object getDwfl() {
        return dwfl;
    }

    public void setDwfl(Object dwfl) {
        this.dwfl = dwfl;
    }

    public Object getOffice() {
        return office;
    }

    public void setOffice(Object office) {
        this.office = office;
    }

    public Object getDwdh() {
        return dwdh;
    }

    public void setDwdh(Object dwdh) {
        this.dwdh = dwdh;
    }

    public Object getDwfax() {
        return dwfax;
    }

    public void setDwfax(Object dwfax) {
        this.dwfax = dwfax;
    }

    public Object getDwlxr() {
        return dwlxr;
    }

    public void setDwlxr(Object dwlxr) {
        this.dwlxr = dwlxr;
    }

    public Object getDwlxrdh() {
        return dwlxrdh;
    }

    public void setDwlxrdh(Object dwlxrdh) {
        this.dwlxrdh = dwlxrdh;
    }

    public Object getDwlxrzd() {
        return dwlxrzd;
    }

    public void setDwlxrzd(Object dwlxrzd) {
        this.dwlxrzd = dwlxrzd;
    }

    public Object getRemarks() {
        return remarks;
    }

    public void setRemarks(Object remarks) {
        this.remarks = remarks;
    }

    public List<WaterBookDetailListBean> getWaterBookDetailList() {
        return waterBookDetailList;
    }

    public void setWaterBookDetailList(List<WaterBookDetailListBean> waterBookDetailList) {
        this.waterBookDetailList = waterBookDetailList;
    }

    public static class WaterBookDetailListBean  implements Serializable {
        /**
         * dwmc : c单位
         * dwfl : 23
         * office : 1
         * dwdh :
         * dwfax :
         * dwlxr :
         * dwlxrdh :
         * dwlxrzd :
         * remarks :
         * waterBookDetailList : null
         */

        private String dwmc;
        private String dwfl;
        private String office;
        private String dwdh;
        private String dwfax;
        private String dwlxr;
        private String dwlxrdh;
        private String dwlxrzd;
        private String remarks;
        @Expose
        private String sortLetters;
        private Object waterBookDetailList;

        public String getDwmc() {
            return dwmc;
        }

        public String getSortLetters() {
            return sortLetters;
        }

        public void setSortLetters(String sortLetters) {
            this.sortLetters = sortLetters;
        }

        public void setDwmc(String dwmc) {
            this.dwmc = dwmc;
        }

        public String getDwfl() {
            return dwfl;
        }

        public void setDwfl(String dwfl) {
            this.dwfl = dwfl;
        }

        public String getOffice() {
            return office;
        }

        public void setOffice(String office) {
            this.office = office;
        }

        public String getDwdh() {
            return dwdh;
        }

        public void setDwdh(String dwdh) {
            this.dwdh = dwdh;
        }

        public String getDwfax() {
            return dwfax;
        }

        public void setDwfax(String dwfax) {
            this.dwfax = dwfax;
        }

        public String getDwlxr() {
            return dwlxr;
        }

        public void setDwlxr(String dwlxr) {
            this.dwlxr = dwlxr;
        }

        public String getDwlxrdh() {
            return dwlxrdh;
        }

        public void setDwlxrdh(String dwlxrdh) {
            this.dwlxrdh = dwlxrdh;
        }

        public String getDwlxrzd() {
            return dwlxrzd;
        }

        public void setDwlxrzd(String dwlxrzd) {
            this.dwlxrzd = dwlxrzd;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public Object getWaterBookDetailList() {
            return waterBookDetailList;
        }

        public void setWaterBookDetailList(Object waterBookDetailList) {
            this.waterBookDetailList = waterBookDetailList;
        }
    }
}
