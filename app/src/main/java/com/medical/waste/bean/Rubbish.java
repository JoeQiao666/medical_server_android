package com.medical.waste.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.greenrobot.greendao.annotation.Generated;
@Entity
public class Rubbish implements Serializable {

    private static final long serialVersionUID = -6882251215011759580L;
    /**
     * id : 5249ca0c8f034fb890c3ca38218acaef
     * departmentId : 0
     * weight : 1
     * typeId : 0
     * status : 0
     * operatorId :
     * staffId : 1
     * administratorId :
     * recyclerId : 0
     * companyId :
     * isBottle : false
     * opBy : 0
     * opAt : 1563553890
     * delFlag : false
     * storeAt :
     * recycleAt :
     * createdTime : 2019-07-20 00:31:30
     * departmentName : 输液大厅
     * typeName : 感染性
     * staffName : 护士
     * administratorName :
     * recyclerName : 回收员
     * companyName :
     * exception :
     */
    @Id
    private String id;
    private String departmentId;
    private String weight;
    private String typeId;
    private int status;
    private String operatorId;
    private String staffId;
    private String administratorId;
    private String recyclerId;
    private String companyId;
    private boolean isBottle;
    private String opBy;
    private String opAt;
    private boolean delFlag;
    private String storeAt;
    private String recycleAt;
    private String createdTime;
    private String departmentName;
    private String typeName;
    private String staffName;
    private String administratorName;
    private String recyclerName;
    private String companyName;
    private String exception;
    private long updateTime = -1;
    @Transient
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Transient
    private SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");

    @Generated(hash = 2051998678)
    public Rubbish(String id, String departmentId, String weight, String typeId,
            int status, String operatorId, String staffId, String administratorId,
            String recyclerId, String companyId, boolean isBottle, String opBy,
            String opAt, boolean delFlag, String storeAt, String recycleAt,
            String createdTime, String departmentName, String typeName,
            String staffName, String administratorName, String recyclerName,
            String companyName, String exception, long updateTime) {
        this.id = id;
        this.departmentId = departmentId;
        this.weight = weight;
        this.typeId = typeId;
        this.status = status;
        this.operatorId = operatorId;
        this.staffId = staffId;
        this.administratorId = administratorId;
        this.recyclerId = recyclerId;
        this.companyId = companyId;
        this.isBottle = isBottle;
        this.opBy = opBy;
        this.opAt = opAt;
        this.delFlag = delFlag;
        this.storeAt = storeAt;
        this.recycleAt = recycleAt;
        this.createdTime = createdTime;
        this.departmentName = departmentName;
        this.typeName = typeName;
        this.staffName = staffName;
        this.administratorName = administratorName;
        this.recyclerName = recyclerName;
        this.companyName = companyName;
        this.exception = exception;
        this.updateTime = updateTime;
    }

    @Generated(hash = 1584895825)
    public Rubbish() {
    }

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getAdministratorId() {
        return administratorId;
    }

    public void setAdministratorId(String administratorId) {
        this.administratorId = administratorId;
    }

    public String getRecyclerId() {
        return recyclerId;
    }

    public void setRecyclerId(String recyclerId) {
        this.recyclerId = recyclerId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public boolean isIsBottle() {
        return isBottle;
    }

    public void setIsBottle(boolean isBottle) {
        this.isBottle = isBottle;
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

    public String getStoreAt() {
        return storeAt;
    }

    public void setStoreAt(String storeAt) {
        this.storeAt = storeAt;
    }

    public String getRecycleAt() {
        return recycleAt;
    }

    public void setRecycleAt(String recycleAt) {
        this.recycleAt = recycleAt;
    }

    public String getCreatedTime() {
        return createdTime;
    }
    public String getCreatedTime1() {
        try {
            return format1.format(format.parse(createdTime).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getAdministratorName() {
        return administratorName;
    }

    public void setAdministratorName(String administratorName) {
        this.administratorName = administratorName;
    }

    public String getRecyclerName() {
        return recyclerName;
    }

    public void setRecyclerName(String recyclerName) {
        this.recyclerName = recyclerName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public boolean getIsBottle() {
        return this.isBottle;
    }

    public boolean getDelFlag() {
        return this.delFlag;
    }

    public boolean isBottle() {
        return isBottle;
    }

    public void setBottle(boolean bottle) {
        isBottle = bottle;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
