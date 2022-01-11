package com.ab.hicarerun.network.models.attachmentmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 1/6/2020.
 */
public class MSTAttachment {
    @SerializedName("TaskNo")
    @Expose
    private String TaskNo;

    @SerializedName("TaskType")
    @Expose
    private String TaskType;

    @SerializedName("AttachmentList")
    @Expose
    private List<GetAttachmentList> AttachmentList = null;

    public String getTaskNo() {
        return TaskNo;
    }

    public void setTaskNo(String taskNo) {
        TaskNo = taskNo;
    }

    public String getTaskType() {
        return TaskType;
    }

    public void setTaskType(String taskType) {
        TaskType = taskType;
    }

    public List<GetAttachmentList> getAttachmentList() {
        return AttachmentList;
    }

    public void setAttachmentList(List<GetAttachmentList> attachmentList) {
        AttachmentList = attachmentList;
    }
}
