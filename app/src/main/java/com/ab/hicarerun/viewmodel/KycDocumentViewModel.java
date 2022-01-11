package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.kycmodel.DocumentData;

/**
 * Created by Arjun Bhatt on 9/23/2020.
 */
public class KycDocumentViewModel implements Parcelable {
    private int id;
    private String resourceId;
    private String Record_Type;
    private String Document_Url;
    private String Document_No;
    private String Created_On;

    public KycDocumentViewModel() {
        this.id = 0;
        this.resourceId = "NA";
        this.Record_Type = "NA";
        this.Document_Url = "NA";
        this.Document_No = "NA";
        this.Created_On = "NA";
    }


    protected KycDocumentViewModel(Parcel in) {
        id = in.readInt();
        resourceId = in.readString();
        Record_Type = in.readString();
        Document_Url = in.readString();
        Document_No = in.readString();
        Created_On = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(resourceId);
        dest.writeString(Record_Type);
        dest.writeString(Document_Url);
        dest.writeString(Document_No);
        dest.writeString(Created_On);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<KycDocumentViewModel> CREATOR = new Creator<KycDocumentViewModel>() {
        @Override
        public KycDocumentViewModel createFromParcel(Parcel in) {
            return new KycDocumentViewModel(in);
        }

        @Override
        public KycDocumentViewModel[] newArray(int size) {
            return new KycDocumentViewModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getRecord_Type() {
        return Record_Type;
    }

    public void setRecord_Type(String record_Type) {
        Record_Type = record_Type;
    }

    public String getDocument_Url() {
        return Document_Url;
    }

    public void setDocument_Url(String document_Url) {
        Document_Url = document_Url;
    }

    public String getCreated_On() {
        return Created_On;
    }

    public void setCreated_On(String created_On) {
        Created_On = created_On;
    }


    public String getDocument_No() {
        return Document_No;
    }

    public void setDocument_No(String document_No) {
        Document_No = document_No;
    }

    public void clone(DocumentData rewards) {
        this.id = rewards.getId();
        this.resourceId = rewards.getResourceId();
        this.Record_Type = rewards.getRecordType();
        this.Document_Url = rewards.getDocumentUrl();
        this.Created_On = rewards.getCreatedOn();
        this.Document_No = rewards.getDocument_No();
    }
}


