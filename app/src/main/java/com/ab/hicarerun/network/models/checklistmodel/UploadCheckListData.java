package com.ab.hicarerun.network.models.checklistmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 7/17/2020.
 */
public class UploadCheckListData {
    @SerializedName("TaskId")
    @Expose
    private String taskId;
    @SerializedName("ResourceId")
    @Expose
    private String resourceId;
    @SerializedName("FileName")
    @Expose
    private String fileName;
    @SerializedName("FileContent")
    @Expose
    private Object fileContent;
    @SerializedName("FileUrl")
    @Expose
    private String fileUrl;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Object getFileContent() {
        return fileContent;
    }

    public void setFileContent(Object fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
