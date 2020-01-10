package com.ab.hicarerun.network.models.OnSiteModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 12/23/2019.
 */
public class OnSiteRecent  {
    @SerializedName("Id")
    @Expose
    private Integer id;
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
    @SerializedName("ServiceType")
    @Expose
    private String ServiceType;
    @SerializedName("AreaType")
    @Expose
    private String areaType;
    @SerializedName("AreaSubType")
    @Expose
    private String areaSubType;
    @SerializedName("CreatedBy")
    @Expose
    private String createdBy;
    @SerializedName("ModifiedBy")
    @Expose
    private String modifiedBy;
    @SerializedName("AccountActivity")
    @Expose
    private String accountActivity;
    @SerializedName("ActivityDetail")
    @Expose
    private List<RecentActivityDetails> activityDetail = null;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getAccountActivity() {
        return accountActivity;
    }

    public void setAccountActivity(String accountActivity) {
        this.accountActivity = accountActivity;
    }

    public List<RecentActivityDetails> getActivityDetail() {
        return activityDetail;
    }

    public void setActivityDetail(List<RecentActivityDetails> activityDetail) {
        this.activityDetail = activityDetail;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }


}
