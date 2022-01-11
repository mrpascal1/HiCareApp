package com.ab.hicarerun.network.models.onsitemodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 12/18/2019.
 */
public class OnSiteAccounts implements Parcelable {
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Account__c")
    @Expose
    private String accountC;
    @SerializedName("Resource__c")
    @Expose
    private Object resourceC;
    @SerializedName("Account__r")
    @Expose
    private Account account;


    protected OnSiteAccounts(Parcel in) {
        id = in.readString();
        name = in.readString();
        accountC = in.readString();
    }

    public static final Creator<OnSiteAccounts> CREATOR = new Creator<OnSiteAccounts>() {
        @Override
        public OnSiteAccounts createFromParcel(Parcel in) {
            return new OnSiteAccounts(in);
        }

        @Override
        public OnSiteAccounts[] newArray(int size) {
            return new OnSiteAccounts[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountC() {
        return accountC;
    }

    public void setAccountC(String accountC) {
        this.accountC = accountC;
    }

    public Object getResourceC() {
        return resourceC;
    }

    public void setResourceC(Object resourceC) {
        this.resourceC = resourceC;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccountR(Account account) {
        this.account = account;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(accountC);
    }
}
