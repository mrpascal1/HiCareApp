package com.ab.hicarerun.network.models.GeneralModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Arjun Bhatt on 1/2/2020.
 */
public class MSTTasks extends RealmObject {
    @SerializedName("TaskId")
    @Expose
    private String TaskId;

    public String getTaskId() {
        return TaskId;
    }

    public void setTaskId(String taskId) {
        TaskId = taskId;
    }
}
