package com.ab.hicarerun.network.models.GeneralModel;

import com.ab.hicarerun.network.models.TechnicianRoutineModel.ValueData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Arjun Bhatt on 4/6/2020.
 */
public class TaskCheckList extends RealmObject {
    @SerializedName("Cid")
    @Expose
    private Integer cid;
    @SerializedName("ServiceType")
    @Expose
    private String serviceType;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Display_Title")
    @Expose
    private String Display_Title;
    @SerializedName("DetailDescription")
    @Expose
    private String detailDescription;
    @SerializedName("IconUrl")
    @Expose
    private String iconUrl;
    @SerializedName("UserOptions")
    @Expose
    private String userOptions;
    @SerializedName("Options")
    @Expose
    private RealmList<ValueData> options = null;
    @SerializedName("Is_Active")
    @Expose
    private Boolean isActive;
    @SerializedName("Take_Picture")
    @Expose
    private Boolean takePicture;

    @SerializedName("IsImageRequired")
    @Expose
    private Boolean isImageRequired;

    private int selectedItem;
    @SerializedName("OptionType")
    @Expose
    private String OptionType;
    @SerializedName("TaskId")
    @Expose
    private String TaskId;
    @SerializedName("ResourceId")
    @Expose
    private String ResourceId;
    @SerializedName("ImagePath")
    @Expose
    private String ImagePath;
    @SerializedName("SelectedAnswer")
    @Expose
    private String SelectedAnswer;

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetailDescription() {
        return detailDescription;
    }

    public void setDetailDescription(String detailDescription) {
        this.detailDescription = detailDescription;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getUserOptions() {
        return userOptions;
    }

    public void setUserOptions(String userOptions) {
        this.userOptions = userOptions;
    }

    public RealmList<ValueData> getOptions() {
        return options;
    }

    public void setOptions(RealmList<ValueData> options) {
        this.options = options;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getTakePicture() {
        return takePicture;
    }

    public void setTakePicture(Boolean takePicture) {
        this.takePicture = takePicture;
    }


    public int getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    public String getOptionType() {
        return OptionType;
    }

    public void setOptionType(String optionType) {
        OptionType = optionType;
    }

    public String getTaskId() {
        return TaskId;
    }

    public void setTaskId(String taskId) {
        TaskId = taskId;
    }

    public String getResourceId() {
        return ResourceId;
    }

    public void setResourceId(String resourceId) {
        ResourceId = resourceId;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getSelectedAnswer() {
        return SelectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        SelectedAnswer = selectedAnswer;
    }

    public Boolean getImageRequired() {
        return isImageRequired;
    }

    public void setImageRequired(Boolean imageRequired) {
        isImageRequired = imageRequired;
    }

    public String getDisplay_Title() {
        return Display_Title;
    }

    public void setDisplay_Title(String display_Title) {
        Display_Title = display_Title;
    }
}
