package com.ab.hicarerun.network.models.OnSiteModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 12/24/2019.
 */
public class RecentActivityDetails implements Parcelable {
    @SerializedName("Id")
    @Expose
    private Integer Id;
    @SerializedName("Activity_Id")
    @Expose
    private Integer activityId;
    @SerializedName("Service_Type")
    @Expose
    private String serviceType;
    @SerializedName("Location_Lat")
    @Expose
    private String lat;
    @SerializedName("Location_Lon")
    @Expose
    private String lon;
    @SerializedName("Start_Time_Text")
    @Expose
    private String startTime;
    @SerializedName("End_Time_Text")
    @Expose
    private String endTime;
    @SerializedName("Is_Service_Done")
    @Expose
    private Boolean isServiceDone;
    @SerializedName("Service_Not_Done_Reason")
    @Expose
    private String serviceNotDoneReason;
    @SerializedName("CreatedBy")
    @Expose
    private Integer createdBy;
    @SerializedName("ModifiedBy")
    @Expose
    private Integer modifiedBy;

    protected RecentActivityDetails(Parcel in) {
        if (in.readByte() == 0) {
            Id = null;
        } else {
            Id = in.readInt();
        }
        if (in.readByte() == 0) {
            activityId = null;
        } else {
            activityId = in.readInt();
        }
        serviceType = in.readString();
        lat = in.readString();
        lon = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        byte tmpIsServiceDone = in.readByte();
        isServiceDone = tmpIsServiceDone == 0 ? null : tmpIsServiceDone == 1;
        serviceNotDoneReason = in.readString();
        if (in.readByte() == 0) {
            createdBy = null;
        } else {
            createdBy = in.readInt();
        }
        if (in.readByte() == 0) {
            modifiedBy = null;
        } else {
            modifiedBy = in.readInt();
        }
    }

    public static final Creator<RecentActivityDetails> CREATOR = new Creator<RecentActivityDetails>() {
        @Override
        public RecentActivityDetails createFromParcel(Parcel in) {
            return new RecentActivityDetails(in);
        }

        @Override
        public RecentActivityDetails[] newArray(int size) {
            return new RecentActivityDetails[size];
        }
    };

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Boolean getIsServiceDone() {
        return isServiceDone;
    }

    public void setIsServiceDone(Boolean isServiceDone) {
        this.isServiceDone = isServiceDone;
    }

    public String getServiceNotDoneReason() {
        return serviceNotDoneReason;
    }

    public void setServiceNotDoneReason(String serviceNotDoneReason) {
        this.serviceNotDoneReason = serviceNotDoneReason;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (Id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(Id);
        }
        if (activityId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(activityId);
        }
        parcel.writeString(serviceType);
        parcel.writeString(lat);
        parcel.writeString(lon);
        parcel.writeString(startTime);
        parcel.writeString(endTime);
        parcel.writeByte((byte) (isServiceDone == null ? 0 : isServiceDone ? 1 : 2));
        parcel.writeString(serviceNotDoneReason);
        if (createdBy == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(createdBy);
        }
        if (modifiedBy == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(modifiedBy);
        }
    }
}
