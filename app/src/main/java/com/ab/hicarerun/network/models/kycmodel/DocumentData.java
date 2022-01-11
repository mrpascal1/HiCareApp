package com.ab.hicarerun.network.models.kycmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 9/23/2020.
 */
public class DocumentData {
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Resource_Id")
    @Expose
    private String resourceId;
    @SerializedName("Record_Type")
    @Expose
    private String recordType;
    @SerializedName("Document_Url")
    @Expose
    private String documentUrl;
    @SerializedName("Document_No")
    @Expose
    private String Document_No;
    @SerializedName("Created_On")
    @Expose
    private String createdOn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getDocument_No() {
        return Document_No;
    }

    public void setDocument_No(String document_No) {
        Document_No = document_No;
    }
}
