package com.ab.hicarerun.network.models.checklistmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 7/16/2020.
 */
public class UploadCheckListRequest {
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
    private String fileContent;
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

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
