package com.ab.hicarerun.network.models.TaskModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tasks implements Parcelable {
    @SerializedName("AccountName")
    @Expose
    private String AccountName;
    @SerializedName("ServicePlan")
    @Expose
    private String ServicePlan;

    @SerializedName("ServiceType")
    @Expose
    private String ServiceType;

    @SerializedName("Tag")
    @Expose
    private String Tag;

    @SerializedName("SequenceNumber")
    @Expose
    private int SequenceNumber;

    @SerializedName("SchedulingStatus")
    @Expose
    private String Status;

    @SerializedName("TaskId")
    @Expose
    private String TaskId;

    @SerializedName("WingFlatOrUnitNumber")
    @Expose
    private String WingFlatOrUnitNumber;

    @SerializedName("BuildingName")
    @Expose
    private String BuildingName;

    @SerializedName("Standard_Property")
    @Expose
    private String StandardProperty;

    @SerializedName("Locality")
    @Expose
    private String Locality;

    @SerializedName("Landmark")
    @Expose
    private String Landmark;

    @SerializedName("Street")
    @Expose
    private String Street;

    @SerializedName("District")
    @Expose
    private String District;

    @SerializedName("Country")
    @Expose
    private String Country;

    @SerializedName("Amount")
    @Expose
    private String Amount;

    @SerializedName("PostalCode")
    @Expose
    private String PostalCode;

    @SerializedName("OrderNumber")
    @Expose
    private String OrderNumber;

    @SerializedName("CombinedOrderNumber")
    @Expose
    private String CombinedOrderNumber;

    @SerializedName("Customer_Instructions")
    @Expose
    private String Customer_Instructions;

    @SerializedName("TaskAssignmentStartDate")
    @Expose
    private String TaskAssignmentStartDate;

    @SerializedName("TaskAssignmentStartTime")
    @Expose
    private String TaskAssignmentStartTime;

    @SerializedName("TaskAssignmentEndDate")
    @Expose
    private String TaskAssignmentEndDate;

    @SerializedName("TaskAssignmentEndTime")
    @Expose
    private String TaskAssignmentEndTime;

    @SerializedName("AssignmentStartDate")
    @Expose
    private String AssignmentStartDate;

    @SerializedName("AssignmentEndDate")
    @Expose
    private String AssignmentEndDate;

    @SerializedName("MobileNo")
    @Expose
    private String MobileNo;

    @SerializedName("AlternateMobileNo")
    @Expose
    private String AlternateMobileNo;

    @SerializedName("TechnicianMobileNo")
    @Expose
    private String TechnicianMobileNo;

    @SerializedName("ResourceLatitude")
    @Expose
    private String AccountLat;

    @SerializedName("ResourceLongitude")
    @Expose
    private String AccountLong;

    @SerializedName("OnsiteLat")
    @Expose
    private String OnsiteLat;

    @SerializedName("OnsiteLong")
    @Expose
    private String OnsiteLong;

    @SerializedName("CompletedLat")
    @Expose
    private String CompletedLat;

    @SerializedName("CompletedLong")
    @Expose
    private String CompletedLong;

    @SerializedName("CustomerLatitude")
    @Expose
    private String CustomerLatitude;

    @SerializedName("CustomerLongitude")
    @Expose
    private String CustomerLongitude;

    @SerializedName("AccountType")
    @Expose
    private String AccountType;

    @SerializedName("CombinedServiceType")
    @Expose
    private String CombinedServiceType;

    @SerializedName("CombinedTaskId")
    @Expose
    private String CombinedTaskId;

    @SerializedName("IsDetailVisible")
    @Expose
    private Boolean IsDetailVisible;

    @SerializedName("IsCombinedTask")
    @Expose
    private Boolean IsCombinedTask;

    @SerializedName("Helper_Resource_Id")
    @Expose
    private String Helper_Resource_Id;

    @SerializedName("Next_Task_Id")
    @Expose
    private String Next_Task_Id;

    @SerializedName("IsTaskToBeHighlighted")
    @Expose
    private Boolean IsTaskToBeHighlighted;

    @SerializedName("ColorForTheTaskToBeHighlighted")
    @Expose
    private String ColorForTheTaskToBeHighlighted;

    @SerializedName("ReasonForHighlightedTask")
    @Expose
    private String ReasonForHighlightedTask;

    public Tasks() {
        AccountName = "NA";
        ServicePlan = "NA";
        ServiceType = "NA";
        Status = "NA";
        TaskId = "NA";
        WingFlatOrUnitNumber = "NA";
        BuildingName = "NA";
        Locality = "NA";
        Landmark = "NA";
        Street = "NA";
        District = "NA";
        Country = "NA";
        Amount = "NA";
        PostalCode = "NA";
        OrderNumber = "NA";
        TaskAssignmentStartDate = "NA";
        TaskAssignmentStartTime = "NA";
        TaskAssignmentEndDate = "NA";
        TaskAssignmentEndTime = "NA";
        AssignmentStartDate = "NA";
        AssignmentEndDate = "NA";
        MobileNo = "NA";
        AlternateMobileNo = "NA";
        StandardProperty = "NA";
        TechnicianMobileNo = "NA";
        AccountLat = "NA";
        AccountLong = "NA";
        OnsiteLat = "NA";
        OnsiteLong = "NA";
        CompletedLat = "NA";
        CompletedLong = "NA";
        CustomerLatitude = "NA";
        CustomerLongitude = "NA";
        CombinedServiceType = "NA";
        Tag = "NA";
        SequenceNumber = 0;
        IsDetailVisible = false;
        IsCombinedTask = false;
        AccountType = "NA";
        CombinedTaskId = "NA";
        CombinedOrderNumber = "NA";
        Helper_Resource_Id = "NA";
        Next_Task_Id = "NA";
        Customer_Instructions = "NA";
        IsTaskToBeHighlighted = false;
        ColorForTheTaskToBeHighlighted = "NA";
        ReasonForHighlightedTask = "NA";
    }


    protected Tasks(Parcel in) {
        AccountName = in.readString();
        ServicePlan = in.readString();
        ServiceType = in.readString();
        Tag = in.readString();
        SequenceNumber = in.readInt();
        Status = in.readString();
        TaskId = in.readString();
        WingFlatOrUnitNumber = in.readString();
        BuildingName = in.readString();
        StandardProperty = in.readString();
        Locality = in.readString();
        Landmark = in.readString();
        Street = in.readString();
        District = in.readString();
        Country = in.readString();
        Amount = in.readString();
        PostalCode = in.readString();
        OrderNumber = in.readString();
        CombinedOrderNumber = in.readString();
        Customer_Instructions = in.readString();
        TaskAssignmentStartDate = in.readString();
        TaskAssignmentStartTime = in.readString();
        TaskAssignmentEndDate = in.readString();
        TaskAssignmentEndTime = in.readString();
        AssignmentStartDate = in.readString();
        AssignmentEndDate = in.readString();
        MobileNo = in.readString();
        AlternateMobileNo = in.readString();
        TechnicianMobileNo = in.readString();
        AccountLat = in.readString();
        AccountLong = in.readString();
        OnsiteLat = in.readString();
        OnsiteLong = in.readString();
        CompletedLat = in.readString();
        CompletedLong = in.readString();
        CustomerLatitude = in.readString();
        CustomerLongitude = in.readString();
        AccountType = in.readString();
        CombinedServiceType = in.readString();
        CombinedTaskId = in.readString();
        byte tmpIsDetailVisible = in.readByte();
        IsDetailVisible = tmpIsDetailVisible == 0 ? null : tmpIsDetailVisible == 1;
        byte tmpIsCombinedTask = in.readByte();
        IsCombinedTask = tmpIsCombinedTask == 0 ? null : tmpIsCombinedTask == 1;
        Helper_Resource_Id = in.readString();
        Next_Task_Id = in.readString();
        byte tmpIsTaskToBeHighlighted = in.readByte();
        IsTaskToBeHighlighted = tmpIsTaskToBeHighlighted == 0 ? null : tmpIsTaskToBeHighlighted == 1;
        ColorForTheTaskToBeHighlighted = in.readString();
        ReasonForHighlightedTask = in.readString();
    }

    public static final Creator<Tasks> CREATOR = new Creator<Tasks>() {
        @Override
        public Tasks createFromParcel(Parcel in) {
            return new Tasks(in);
        }

        @Override
        public Tasks[] newArray(int size) {
            return new Tasks[size];
        }
    };

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getWingFlatOrUnitNumber() {
        return WingFlatOrUnitNumber;
    }

    public void setWingFlatOrUnitNumber(String wingFlatOrUnitNumber) {
        WingFlatOrUnitNumber = wingFlatOrUnitNumber;
    }

    public String getBuildingName() {
        return BuildingName;
    }

    public void setBuildingName(String buildingName) {
        BuildingName = buildingName;
    }

    public String getStandardProperty() {
        return StandardProperty;
    }

    public void setStandardProperty(String standardProperty) {
        StandardProperty = standardProperty;
    }

    public String getLocality() {
        return Locality;
    }

    public void setLocality(String locality) {
        Locality = locality;
    }

    public String getLandmark() {
        return Landmark;
    }

    public void setLandmark(String landmark) {
        Landmark = landmark;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getTaskAssignmentStartDate() {
        return TaskAssignmentStartDate;
    }

    public void setTaskAssignmentStartDate(String taskAssignmentStartDate) {
        TaskAssignmentStartDate = taskAssignmentStartDate;
    }

    public String getTaskAssignmentStartTime() {
        return TaskAssignmentStartTime;
    }

    public void setTaskAssignmentStartTime(String taskAssignmentStartTime) {
        TaskAssignmentStartTime = taskAssignmentStartTime;
    }

    public String getTaskAssignmentEndDate() {
        return TaskAssignmentEndDate;
    }

    public void setTaskAssignmentEndDate(String taskAssignmentEndDate) {
        TaskAssignmentEndDate = taskAssignmentEndDate;
    }

    public String getTaskAssignmentEndTime() {
        return TaskAssignmentEndTime;
    }

    public void setTaskAssignmentEndTime(String taskAssignmentEndTime) {
        TaskAssignmentEndTime = taskAssignmentEndTime;
    }

    public String getAssignmentStartDate() {
        return AssignmentStartDate;
    }

    public void setAssignmentStartDate(String assignmentStartDate) {
        AssignmentStartDate = assignmentStartDate;
    }

    public String getAssignmentEndDate() {
        return AssignmentEndDate;
    }

    public void setAssignmentEndDate(String assignmentEndDate) {
        AssignmentEndDate = assignmentEndDate;
    }

    public String getServicePlan() {
        return ServicePlan;
    }

    public void setServicePlan(String servicePlan) {
        ServicePlan = servicePlan;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTaskId() {
        return TaskId;
    }

    public void setTaskId(String taskId) {
        TaskId = taskId;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getAlternateMobileNo() {
        return AlternateMobileNo;
    }

    public void setAlternateMobileNo(String alternateMobileNo) {
        AlternateMobileNo = alternateMobileNo;
    }

    public String getTechnicianMobileNo() {
        return TechnicianMobileNo;
    }

    public void setTechnicianMobileNo(String technicianMobileNo) {
        TechnicianMobileNo = technicianMobileNo;
    }

    public String getAccountLat() {
        return AccountLat;
    }

    public void setAccountLat(String accountLat) {
        AccountLat = accountLat;
    }

    public String getAccountLong() {
        return AccountLong;
    }

    public void setAccountLong(String accountLong) {
        AccountLong = accountLong;
    }

    public String getOnsiteLat() {
        return OnsiteLat;
    }

    public void setOnsiteLat(String onsiteLat) {
        OnsiteLat = onsiteLat;
    }

    public String getOnsiteLong() {
        return OnsiteLong;
    }

    public void setOnsiteLong(String onsiteLong) {
        OnsiteLong = onsiteLong;
    }

    public String getCompletedLat() {
        return CompletedLat;
    }

    public void setCompletedLat(String completedLat) {
        CompletedLat = completedLat;
    }

    public String getCompletedLong() {
        return CompletedLong;
    }

    public void setCompletedLong(String completedLong) {
        CompletedLong = completedLong;
    }

    public String getCustomerLatitude() {
        return CustomerLatitude;
    }

    public void setCustomerLatitude(String customerLatitude) {
        CustomerLatitude = customerLatitude;
    }

    public String getCustomerLongitude() {
        return CustomerLongitude;
    }

    public void setCustomerLongitude(String customerLongitude) {
        CustomerLongitude = customerLongitude;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public int getSequenceNumber() {
        return SequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        SequenceNumber = sequenceNumber;
    }

    public Boolean getDetailVisible() {
        return IsDetailVisible;
    }

    public void setDetailVisible(Boolean detailVisible) {
        IsDetailVisible = detailVisible;
    }

    public Boolean getCombinedTask() {
        return IsCombinedTask;
    }

    public void setCombinedTask(Boolean combinedTask) {
        IsCombinedTask = combinedTask;
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    public String getCombinedServiceType() {
        return CombinedServiceType;
    }

    public void setCombinedServiceType(String combinedServiceType) {
        CombinedServiceType = combinedServiceType;
    }

    public String getCombinedTaskId() {
        return CombinedTaskId;
    }

    public void setCombinedTaskId(String combinedTaskId) {
        CombinedTaskId = combinedTaskId;
    }

    public String getCombinedOrderNumber() {
        return CombinedOrderNumber;
    }

    public void setCombinedOrderNumber(String combinedOrderNumber) {
        CombinedOrderNumber = combinedOrderNumber;
    }

    public String getHelper_Resource_Id() {
        return Helper_Resource_Id;
    }

    public void setHelper_Resource_Id(String helper_Resource_Id) {
        Helper_Resource_Id = helper_Resource_Id;
    }

    public String getNext_Task_Id() {
        return Next_Task_Id;
    }

    public void setNext_Task_Id(String next_Task_Id) {
        Next_Task_Id = next_Task_Id;
    }

    public String getCustomer_Instructions() {
        return Customer_Instructions;
    }

    public void setCustomer_Instructions(String customer_Instructions) {
        Customer_Instructions = customer_Instructions;
    }

    public Boolean getIsTaskToBeHighlighted() {
        return IsTaskToBeHighlighted;
    }

    public void setIsTaskToBeHighlighted(Boolean isTaskToBeHighlighted) {
        IsTaskToBeHighlighted = isTaskToBeHighlighted;
    }

    public String getColorForTheTaskToBeHighlighted() {
        return ColorForTheTaskToBeHighlighted;
    }

    public void setColorForTheTaskToBeHighlighted(String colorForTheTaskToBeHighlighted) {
        ColorForTheTaskToBeHighlighted = colorForTheTaskToBeHighlighted;
    }

    public String getReasonForHighlightedTask() {
        return ReasonForHighlightedTask;
    }

    public void setReasonForHighlightedTask(String reasonForHighlightedTask) {
        ReasonForHighlightedTask = reasonForHighlightedTask;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(AccountName);
        dest.writeString(ServicePlan);
        dest.writeString(ServiceType);
        dest.writeString(Tag);
        dest.writeInt(SequenceNumber);
        dest.writeString(Status);
        dest.writeString(TaskId);
        dest.writeString(WingFlatOrUnitNumber);
        dest.writeString(BuildingName);
        dest.writeString(StandardProperty);
        dest.writeString(Locality);
        dest.writeString(Landmark);
        dest.writeString(Street);
        dest.writeString(District);
        dest.writeString(Country);
        dest.writeString(Amount);
        dest.writeString(PostalCode);
        dest.writeString(OrderNumber);
        dest.writeString(CombinedOrderNumber);
        dest.writeString(Customer_Instructions);
        dest.writeString(TaskAssignmentStartDate);
        dest.writeString(TaskAssignmentStartTime);
        dest.writeString(TaskAssignmentEndDate);
        dest.writeString(TaskAssignmentEndTime);
        dest.writeString(AssignmentStartDate);
        dest.writeString(AssignmentEndDate);
        dest.writeString(MobileNo);
        dest.writeString(AlternateMobileNo);
        dest.writeString(TechnicianMobileNo);
        dest.writeString(AccountLat);
        dest.writeString(AccountLong);
        dest.writeString(OnsiteLat);
        dest.writeString(OnsiteLong);
        dest.writeString(CompletedLat);
        dest.writeString(CompletedLong);
        dest.writeString(CustomerLatitude);
        dest.writeString(CustomerLongitude);
        dest.writeString(AccountType);
        dest.writeString(CombinedServiceType);
        dest.writeString(CombinedTaskId);
        dest.writeByte((byte) (IsDetailVisible == null ? 0 : IsDetailVisible ? 1 : 2));
        dest.writeByte((byte) (IsCombinedTask == null ? 0 : IsCombinedTask ? 1 : 2));
        dest.writeString(Helper_Resource_Id);
        dest.writeString(Next_Task_Id);
        dest.writeByte((byte) (IsTaskToBeHighlighted == null ? 0 : IsTaskToBeHighlighted ? 1 : 2));
        dest.writeString(ColorForTheTaskToBeHighlighted);
        dest.writeString(ReasonForHighlightedTask);
    }
}
