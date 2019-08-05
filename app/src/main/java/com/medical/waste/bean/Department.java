package com.medical.waste.bean;

public class Department {

    /**
     * id : 0
     * name : 输液大厅
     * opBy : f30367135feb47c2a37e26f9870ec253
     * opAt : 1562988627
     * delFlag : false
     */

    private String id;
    private String name;
    private String opBy;
    private String opAt;
    private boolean delFlag;

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

    public String getOpBy() {
        return opBy;
    }

    public void setOpBy(String opBy) {
        this.opBy = opBy;
    }

    public String getOpAt() {
        return opAt;
    }

    public void setOpAt(String opAt) {
        this.opAt = opAt;
    }

    public boolean isDelFlag() {
        return delFlag;
    }

    public void setDelFlag(boolean delFlag) {
        this.delFlag = delFlag;
    }
}
