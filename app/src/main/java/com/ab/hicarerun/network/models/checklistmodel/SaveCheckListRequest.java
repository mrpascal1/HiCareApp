package com.ab.hicarerun.network.models.checklistmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 7/15/2020.
 */
public class SaveCheckListRequest {
    @SerializedName("TaskId")
    @Expose
    private String taskId;
    @SerializedName("ResourceId")
    @Expose
    private String resourceId;
    @SerializedName("CheckListId")
    @Expose
    private Integer checkListId;
    @SerializedName("OptionName")
    @Expose
    private String optionName;
    @SerializedName("ImagePath")
    @Expose
    private String imagePath;
    private Boolean imageRequired;

    public SaveCheckListRequest() {
        this.taskId = "";
        this.resourceId = "";
        this.checkListId = 0;
        this.optionName = "";
        this.imagePath = "";
        this.imageRequired = false;
    }

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

    public Integer getCheckListId() {
        return checkListId;
    }

    public void setCheckListId(Integer checkListId) {
        this.checkListId = checkListId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setImageRequired(Boolean imageRequired) {
        this.imageRequired = imageRequired;
    }

    public Boolean getImageRequired() {
        return imageRequired;
    }
}
