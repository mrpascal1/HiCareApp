package com.ab.hicarerun.network.models.RoutineModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 8/21/2020.
 */
public class TechRoutineData {

    @SerializedName("ResourceId")
    @Expose
    private String ResourceId;

    @SerializedName("RoutineQuestions")
    @Expose
    private List<RoutineQuestion> routineQuestions = null;

    public List<RoutineQuestion> getRoutineQuestions() {
        return routineQuestions;
    }

    public void setRoutineQuestions(List<RoutineQuestion> routineQuestions) {
        this.routineQuestions = routineQuestions;
    }

    public String getResourceId() {
        return ResourceId;
    }

    public void setResourceId(String resourceId) {
        ResourceId = resourceId;
    }
}
