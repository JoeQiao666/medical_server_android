package com.medical.waste.bean;

import com.google.gson.annotations.Expose;

public class UploadData {

    /**
     * departmentId : 0
     * weight : 1
     * typeId : 0
     * recyclerId : 0
     * staffId : 1
     * isBottle : 0
     */

    private String id;
    private String departmentId;
    private String weight;
    private String typeId;
    private String recyclerId;
    private String staffId;
    private int isBottle;
    @Expose
    private String typeName;
    @Expose
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getRecyclerId() {
        return recyclerId;
    }

    public void setRecyclerId(String recyclerId) {
        this.recyclerId = recyclerId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public int getIsBottle() {
        return isBottle;
    }

    public void setIsBottle(int isBottle) {
        this.isBottle = isBottle;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
