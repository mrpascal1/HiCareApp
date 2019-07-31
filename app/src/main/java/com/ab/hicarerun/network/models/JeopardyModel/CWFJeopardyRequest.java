package com.ab.hicarerun.network.models.JeopardyModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CWFJeopardyRequest {
    @SerializedName("taskId")
    @Expose
    private String taskId;

    @SerializedName("jeopardyText")
    @Expose
    private String jeopardyText;

    @SerializedName("batchName")
    @Expose
    private String batchName;

    @SerializedName("remark")
    @Expose
    private String remark;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getJeopardyText() {
        return jeopardyText;
    }

    public void setJeopardyText(String jeopardyText) {
        this.jeopardyText = jeopardyText;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
