package com.ab.hicarerun.network.models.ChemicalModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 1/3/2020.
 */
public class MSTChemicals {
    @SerializedName("TaskNo")
    @Expose
    private String TaskNo;
    @SerializedName("TaskType")
    @Expose
    private String TaskType;
    @SerializedName("ChemicalList")
    @Expose
    private List<Chemicals> data = null;


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

    public List<Chemicals> getData() {
        return data;
    }

    public void setData(List<Chemicals> data) {
        this.data = data;
    }
}
