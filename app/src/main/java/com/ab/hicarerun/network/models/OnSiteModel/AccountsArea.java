package com.ab.hicarerun.network.models.OnSiteModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 12/18/2019.
 */
public class AccountsArea {
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Customer_id__c")
    @Expose
    private String customerIdC;
    @SerializedName("Customer_Referral_Code__c")
    @Expose
    private Object customerReferralCodeC;
    @SerializedName("BillTo_Customer_ID__c")
    @Expose
    private Object billToCustomerIDC;
    @SerializedName("Bill_To_Customer_Number__c")
    @Expose
    private Object billToCustomerNumberC;
    @SerializedName("Mobile__c")
    @Expose
    private String mobileC;
    @SerializedName("Alternate_Mobile__c")
    @Expose
    private Object alternateMobileC;
    @SerializedName("Alternate_Phone__c")
    @Expose
    private Object alternatePhoneC;
    @SerializedName("Phone")
    @Expose
    private Object phone;
    @SerializedName("Account_Type__c")
    @Expose
    private Object accountTypeC;
    @SerializedName("Account_Types__c")
    @Expose
    private Object accountTypesC;
    @SerializedName("Email__c")
    @Expose
    private Object emailC;
    @SerializedName("Flat_Number__c")
    @Expose
    private String flatNumberC;
    @SerializedName("Building_Name__c")
    @Expose
    private String buildingNameC;
    @SerializedName("Landmark__c")
    @Expose
    private Object landmarkC;
    @SerializedName("Locality_Suburb__c")
    @Expose
    private Object localitySuburbC;
    @SerializedName("BillingStreet")
    @Expose
    private String billingStreet;
    @SerializedName("BillingPostalCode")
    @Expose
    private String billingPostalCode;
    @SerializedName("Location__Latitude__s")
    @Expose
    private Double locationLatitudeS;
    @SerializedName("Location__Longitude__s")
    @Expose
    private Double locationLongitudeS;
    @SerializedName("Account_Lat__c")
    @Expose
    private Integer accountLatC;
    @SerializedName("Account_Long__c")
    @Expose
    private Integer accountLongC;
    @SerializedName("IsSelected")
    @Expose
    private Boolean isSelected;
    @SerializedName("Locality_Pincode__r")
    @Expose
    private Object localityPincodeR;
    @SerializedName("Salutation__c")
    @Expose
    private Object salutationC;
    @SerializedName("First_Name__c")
    @Expose
    private String firstNameC;
    @SerializedName("Last_Name__c")
    @Expose
    private String lastNameC;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("SR_Outstanding_Amount__c")
    @Expose
    private Object sROutstandingAmountC;
    @SerializedName("Opt_Out_For_Emails__c")
    @Expose
    private Boolean optOutForEmailsC;
    @SerializedName("Opt_Out_For_SMS__c")
    @Expose
    private Boolean optOutForSMSC;

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

    public String getCustomerIdC() {
        return customerIdC;
    }

    public void setCustomerIdC(String customerIdC) {
        this.customerIdC = customerIdC;
    }

    public Object getCustomerReferralCodeC() {
        return customerReferralCodeC;
    }

    public void setCustomerReferralCodeC(Object customerReferralCodeC) {
        this.customerReferralCodeC = customerReferralCodeC;
    }

    public Object getBillToCustomerIDC() {
        return billToCustomerIDC;
    }

    public void setBillToCustomerIDC(Object billToCustomerIDC) {
        this.billToCustomerIDC = billToCustomerIDC;
    }

    public Object getBillToCustomerNumberC() {
        return billToCustomerNumberC;
    }

    public void setBillToCustomerNumberC(Object billToCustomerNumberC) {
        this.billToCustomerNumberC = billToCustomerNumberC;
    }

    public String getMobileC() {
        return mobileC;
    }

    public void setMobileC(String mobileC) {
        this.mobileC = mobileC;
    }

    public Object getAlternateMobileC() {
        return alternateMobileC;
    }

    public void setAlternateMobileC(Object alternateMobileC) {
        this.alternateMobileC = alternateMobileC;
    }

    public Object getAlternatePhoneC() {
        return alternatePhoneC;
    }

    public void setAlternatePhoneC(Object alternatePhoneC) {
        this.alternatePhoneC = alternatePhoneC;
    }

    public Object getPhone() {
        return phone;
    }

    public void setPhone(Object phone) {
        this.phone = phone;
    }

    public Object getAccountTypeC() {
        return accountTypeC;
    }

    public void setAccountTypeC(Object accountTypeC) {
        this.accountTypeC = accountTypeC;
    }

    public Object getAccountTypesC() {
        return accountTypesC;
    }

    public void setAccountTypesC(Object accountTypesC) {
        this.accountTypesC = accountTypesC;
    }

    public Object getEmailC() {
        return emailC;
    }

    public void setEmailC(Object emailC) {
        this.emailC = emailC;
    }

    public String getFlatNumberC() {
        return flatNumberC;
    }

    public void setFlatNumberC(String flatNumberC) {
        this.flatNumberC = flatNumberC;
    }

    public String getBuildingNameC() {
        return buildingNameC;
    }

    public void setBuildingNameC(String buildingNameC) {
        this.buildingNameC = buildingNameC;
    }

    public Object getLandmarkC() {
        return landmarkC;
    }

    public void setLandmarkC(Object landmarkC) {
        this.landmarkC = landmarkC;
    }

    public Object getLocalitySuburbC() {
        return localitySuburbC;
    }

    public void setLocalitySuburbC(Object localitySuburbC) {
        this.localitySuburbC = localitySuburbC;
    }

    public String getBillingStreet() {
        return billingStreet;
    }

    public void setBillingStreet(String billingStreet) {
        this.billingStreet = billingStreet;
    }

    public String getBillingPostalCode() {
        return billingPostalCode;
    }

    public void setBillingPostalCode(String billingPostalCode) {
        this.billingPostalCode = billingPostalCode;
    }

    public Double getLocationLatitudeS() {
        return locationLatitudeS;
    }

    public void setLocationLatitudeS(Double locationLatitudeS) {
        this.locationLatitudeS = locationLatitudeS;
    }

    public Double getLocationLongitudeS() {
        return locationLongitudeS;
    }

    public void setLocationLongitudeS(Double locationLongitudeS) {
        this.locationLongitudeS = locationLongitudeS;
    }

    public Integer getAccountLatC() {
        return accountLatC;
    }

    public void setAccountLatC(Integer accountLatC) {
        this.accountLatC = accountLatC;
    }

    public Integer getAccountLongC() {
        return accountLongC;
    }

    public void setAccountLongC(Integer accountLongC) {
        this.accountLongC = accountLongC;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Object getLocalityPincodeR() {
        return localityPincodeR;
    }

    public void setLocalityPincodeR(Object localityPincodeR) {
        this.localityPincodeR = localityPincodeR;
    }

    public Object getSalutationC() {
        return salutationC;
    }

    public void setSalutationC(Object salutationC) {
        this.salutationC = salutationC;
    }

    public String getFirstNameC() {
        return firstNameC;
    }

    public void setFirstNameC(String firstNameC) {
        this.firstNameC = firstNameC;
    }

    public String getLastNameC() {
        return lastNameC;
    }

    public void setLastNameC(String lastNameC) {
        this.lastNameC = lastNameC;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Object getSROutstandingAmountC() {
        return sROutstandingAmountC;
    }

    public void setSROutstandingAmountC(Object sROutstandingAmountC) {
        this.sROutstandingAmountC = sROutstandingAmountC;
    }

    public Boolean getOptOutForEmailsC() {
        return optOutForEmailsC;
    }

    public void setOptOutForEmailsC(Boolean optOutForEmailsC) {
        this.optOutForEmailsC = optOutForEmailsC;
    }

    public Boolean getOptOutForSMSC() {
        return optOutForSMSC;
    }

    public void setOptOutForSMSC(Boolean optOutForSMSC) {
        this.optOutForSMSC = optOutForSMSC;
    }

}
