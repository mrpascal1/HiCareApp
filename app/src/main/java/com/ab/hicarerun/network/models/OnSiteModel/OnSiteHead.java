package com.ab.hicarerun.network.models.OnSiteModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Arjun Bhatt on 12/31/2019.
 */
public class OnSiteHead {
    @SerializedName("Head")
    @Expose
    private String Head;
    @SerializedName("Data")
    @Expose
    private List<OnSiteRecent> data = null;

    public String getHead() {
        return Head;
    }

    public void setHead(String head) {
        Head = head;
    }

    public List<OnSiteRecent> getData() {
        return data;
    }

    public void setData(List<OnSiteRecent> data) {
        this.data = data;
    }


}
