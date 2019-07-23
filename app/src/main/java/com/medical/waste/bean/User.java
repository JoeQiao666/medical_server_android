package com.medical.waste.bean;

import java.io.Serializable;

public class User implements Serializable {

    /**
     * id : 0
     * loginname : recycler
     * username : 回收员
     * opBy : 0
     * opAt : 1563552514
     * delFlag : false
     * jobNumber : recycler
     * departmentId : 0
     * permission : query
     * roleId : 1
     * cardId : 0
     * positionName : 垃圾回收员
     * roleName : 回收员
     * departmentName : 输液大厅
     */

    private String id;
    private String loginname;
    private String username;
    private String opBy;
    private String opAt;
    private boolean delFlag;
    private String jobNumber;
    private String departmentId;
    private String permission;
    private String roleId;
    private String cardId;
    private String positionName;
    private String roleName;
    private String departmentName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
