package com.ab.hicarerun.network.models.TechnicianRoutineModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 8/13/2020.
 */
public class RoutineData {
    @SerializedName("RoutineQuestions")
    @Expose
    private List<RoutineQuestions> routineQuestions = null;

    public List<RoutineQuestions> getRoutineQuestions() {
        return routineQuestions;
    }

    public void setRoutineQuestions(List<RoutineQuestions> routineQuestions) {
        this.routineQuestions = routineQuestions;
    }
}
