package com.zyinfo.brj.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/3.
 */

public class waterChannelBeans implements Serializable {


    /**
     * id : 0bc7289bce7c4c77b9948b1752297b15
     * name : 北致富干渠
     * type : 干渠
     * unit :
     * coefficient :
     * cnLength :
     */

    private String id;
    private String name;
    private String type;
    private String unit;
    private String coefficient;
    private String cnLength;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(String coefficient) {
        this.coefficient = coefficient;
    }

    public String getCnLength() {
        return cnLength;
    }

    public void setCnLength(String cnLength) {
        this.cnLength = cnLength;
    }
}
