package com.ab.hicarerun.network.models.covidmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 5/7/2020.
 */
public class CovidRequest {
    @SerializedName("ResourceId")
    @Expose
    private String resourceId;
    @SerializedName("TaskId")
    @Expose
    private String taskId;
    @SerializedName("File")
    @Expose
    private String file;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
