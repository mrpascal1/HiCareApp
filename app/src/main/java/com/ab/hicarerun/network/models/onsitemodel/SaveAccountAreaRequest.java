package com.ab.hicarerun.network.models.onsitemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 12/18/2019.
 */
public class SaveAccountAreaRequest {
    @SerializedName("AccountName")
    @Expose
    private String accountName;
    @SerializedName("AccountId")
    @Expose
    private String accountId;
    @SerializedName("AccountNo")
    @Expose
    private String accountNo;
    @SerializedName("TaskId")
    @Expose
    private String taskId;
    @SerializedName("TaskNo")
    @Expose
    private String taskNo;
    @SerializedName("ServiceRequestId")
    @Expose
    private String serviceRequestId;
    @SerializedName("ServiceRequestNo")
    @Expose
    private String serviceRequestNo;
    @SerializedName("AreaType")
    @Expose
    private String areaType;
    @SerializedName("AreaSubType")
    @Expose
    private String areaSubType;
    @SerializedName("Resource_Id")
    @Expose
    private String Resource_Id;
    @SerializedName("CreatedBy")
    @Expose
    private Integer createdBy;
    @SerializedName("ModifiedBy")
    @Expose
    private Integer modifiedBy;
    @SerializedName("ActivityDetail")
    @Expose
    private List<ActivityDetail> activityDetail = null;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(String serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }

    public String getServiceRequestNo() {
        return serviceRequestNo;
    }

    public void setServiceRequestNo(String serviceRequestNo) {
        this.serviceRequestNo = serviceRequestNo;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getAreaSubType() {
        return areaSubType;
    }

    public void setAreaSubType(String areaSubType) {
        this.areaSubType = areaSubType;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public List<ActivityDetail> getActivityDetail() {
        return activityDetail;
    }

    public void setActivityDetail(List<ActivityDetail> activityDetail) {
        this.activityDetail = activityDetail;
    }

    public String getResource_Id() {
        return Resource_Id;
    }

    public void setResource_Id(String resource_Id) {
        Resource_Id = resource_Id;
    }
}
