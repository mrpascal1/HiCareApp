package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.taskmodel.Tasks;

public class TaskViewModel implements Parcelable {
    private String AccountName;
    private String WingFlatOrUnitNumber;
    private String BuildingName;
    private String StandardProperty;
    private String Locality;
    private String Landmark;
    private String Street;
    private String District;
    private String Country;
    private String Amount;
    private String PostalCode;
    private String OrderNumber;
    private String TaskAssignmentStartDate;
    private String TaskAssignmentStartTime;
    private String TaskAssignmentEndDate;
    private String TaskAssignmentEndTime;
    private String AssignmentStartDate;
    private String AssignmentEndDate;
    private String Status;
    private String TaskId;
    private String ServicePlan;
    private String ServiceType;
    private String primaryMobile;
    private String altMobile;
    private String techMobile;
    private String AccountLat;
    private String AccountLong;
    private String OnsiteLat;
    private String OnsiteLong;
    private String CompletedLat;
    private String CompletedLong;
    private String CustomerLat;
    private String CustomerLong;
    private String Tag;
    private String AccountType;
    private int SequenceNumber;
    private Boolean IsDetailVisible;
    private Boolean IsCombineTask;
    private String CombineTaskType;
    private String CombineOrderNumber;
    private String HelperResourceId;
    private String customerInstructions;
    private Boolean IsTaskToBeHighlighted;
    private String ColorForTheTaskToBeHighlighted;
    private String ReasonForHighlightedTask;

    public TaskViewModel() {
        AccountName = "NA";
        WingFlatOrUnitNumber = "NA";
        BuildingName = "NA";
        StandardProperty = "NA";
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
        ServicePlan = "NA";
        TaskId = "NA";
        Status = "NA";
        ServiceType = "NA";
        primaryMobile = "NA";
        altMobile = "NA";
        techMobile = "NA";
        AccountLat = "NA";
        AccountLong = "NA";
        OnsiteLat = "NA";
        OnsiteLong = "NA";
        CompletedLat = "NA";
        CompletedLong = "NA";
        CustomerLat = "NA";
        CustomerLong = "NA";
        Tag = "NA";
        SequenceNumber = 0;
        IsDetailVisible = false;
        AccountType = "NA";
        IsCombineTask = false;
        CombineTaskType = "NA";
        CombineOrderNumber = "NA";
        customerInstructions = "NA";
        IsTaskToBeHighlighted = false;
        ColorForTheTaskToBeHighlighted = "NA";
        ReasonForHighlightedTask = "NA";
    }


    protected TaskViewModel(Parcel in) {
        AccountName = in.readString();
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
        TaskAssignmentStartDate = in.readString();
        TaskAssignmentStartTime = in.readString();
        TaskAssignmentEndDate = in.readString();
        TaskAssignmentEndTime = in.readString();
        AssignmentStartDate = in.readString();
        AssignmentEndDate = in.readString();
        Status = in.readString();
        TaskId = in.readString();
        ServicePlan = in.readString();
        ServiceType = in.readString();
        primaryMobile = in.readString();
        altMobile = in.readString();
        techMobile = in.readString();
        AccountLat = in.readString();
        AccountLong = in.readString();
        OnsiteLat = in.readString();
        OnsiteLong = in.readString();
        CompletedLat = in.readString();
        CompletedLong = in.readString();
        CustomerLat = in.readString();
        CustomerLong = in.readString();
        Tag = in.readString();
        AccountType = in.readString();
        SequenceNumber = in.readInt();
        byte tmpIsDetailVisible = in.readByte();
        IsDetailVisible = tmpIsDetailVisible == 0 ? null : tmpIsDetailVisible == 1;
        byte tmpIsCombineTask = in.readByte();
        IsCombineTask = tmpIsCombineTask == 0 ? null : tmpIsCombineTask == 1;
        CombineTaskType = in.readString();
        CombineOrderNumber = in.readString();
        HelperResourceId = in.readString();
        customerInstructions = in.readString();
        byte tmpIsTaskToBeHighlighted = in.readByte();
        IsTaskToBeHighlighted = tmpIsTaskToBeHighlighted == 0 ? null : tmpIsTaskToBeHighlighted == 1;
        ColorForTheTaskToBeHighlighted = in.readString();
        ReasonForHighlightedTask = in.readString();
    }

    public static final Creator<TaskViewModel> CREATOR = new Creator<TaskViewModel>() {
        @Override
        public TaskViewModel createFromParcel(Parcel in) {
            return new TaskViewModel(in);
        }

        @Override
        public TaskViewModel[] newArray(int size) {
            return new TaskViewModel[size];
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

    public String getServicePlan() {
        return ServicePlan;
    }

    public void setServicePlan(String servicePlan) {
        ServicePlan = servicePlan;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getPrimaryMobile() {
        return primaryMobile;
    }

    public void setPrimaryMobile(String primaryMobile) {
        this.primaryMobile = primaryMobile;
    }

    public String getAltMobile() {
        return altMobile;
    }

    public void setAltMobile(String altMobile) {
        this.altMobile = altMobile;
    }

    public String getTechMobile() {
        return techMobile;
    }

    public void setTechMobile(String techMobile) {
        this.techMobile = techMobile;
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

    public String getCustomerLat() {
        return CustomerLat;
    }

    public void setCustomerLat(String customerLat) {
        CustomerLat = customerLat;
    }

    public String getCustomerLong() {
        return CustomerLong;
    }

    public void setCustomerLong(String customerLong) {
        CustomerLong = customerLong;
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

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    public Boolean getCombineTask() {
        return IsCombineTask;
    }

    public void setCombineTask(Boolean combineTask) {
        IsCombineTask = combineTask;
    }

    public String getCombineTaskType() {
        return CombineTaskType;
    }

    public void setCombineTaskType(String combineTaskType) {
        CombineTaskType = combineTaskType;
    }

    public String getCombineOrderNumber() {
        return CombineOrderNumber;
    }

    public void setCombineOrderNumber(String combineOrderNumber) {
        CombineOrderNumber = combineOrderNumber;
    }

    public String getHelperResourceId() {
        return HelperResourceId;
    }

    public void setHelperResourceId(String helperResourceId) {
        HelperResourceId = helperResourceId;
    }


    public String getStandardProperty() {
        return StandardProperty;
    }

    public void setStandardProperty(String standardProperty) {
        StandardProperty = standardProperty;
    }

    public String getCustomerInstructions() {
        return customerInstructions;
    }

    public void setCustomerInstructions(String customerInstructions) {
        this.customerInstructions = customerInstructions;
    }

    public Boolean getTaskToBeHighlighted() {
        return IsTaskToBeHighlighted;
    }

    public void setTaskToBeHighlighted(Boolean taskToBeHighlighted) {
        IsTaskToBeHighlighted = taskToBeHighlighted;
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

    public void clone(Tasks tasks) {
        this.AccountName = tasks.getAccountName();
        this.BuildingName = tasks.getBuildingName();
        this.Country = tasks.getCountry();
        this.District = tasks.getDistrict();
        this.WingFlatOrUnitNumber = tasks.getWingFlatOrUnitNumber();
        this.Locality = tasks.getLocality();
        this.Landmark = tasks.getLandmark();
        this.Street = tasks.getStreet();
        this.PostalCode = tasks.getPostalCode();
        this.OrderNumber = tasks.getOrderNumber();
        this.Amount = tasks.getAmount();
        this.AssignmentStartDate = tasks.getAssignmentStartDate();
        this.AssignmentEndDate = tasks.getAssignmentEndDate();
        this.TaskAssignmentStartDate = tasks.getTaskAssignmentStartDate();
        this.TaskAssignmentEndDate = tasks.getTaskAssignmentEndDate();
        this.TaskAssignmentStartTime = tasks.getTaskAssignmentStartTime();
        this.TaskAssignmentEndTime = tasks.getTaskAssignmentEndTime();
        this.Status = tasks.getStatus();
        this.ServicePlan = tasks.getServicePlan();
        this.TaskId = tasks.getTaskId();
        this.ServiceType = tasks.getServiceType();
        this.primaryMobile = tasks.getMobileNo();
        this.altMobile = tasks.getAlternateMobileNo();
        this.techMobile = tasks.getTechnicianMobileNo();
        this.AccountLat = tasks.getAccountLat();
        this.AccountLong = tasks.getAccountLong();
        this.OnsiteLat = tasks.getOnsiteLat();
        this.OnsiteLong = tasks.getOnsiteLong();
        this.CompletedLat = tasks.getCompletedLat();
        this.CompletedLong = tasks.getCompletedLong();
        this.CustomerLat = tasks.getCustomerLatitude();
        this.CustomerLong = tasks.getCustomerLongitude();
        this.Tag = tasks.getTag();
        this.SequenceNumber = tasks.getSequenceNumber();
        this.IsDetailVisible = tasks.getDetailVisible();
        this.AccountType = tasks.getAccountType();
        this.IsCombineTask = tasks.getCombinedTask();
        this.CombineTaskType = tasks.getCombinedServiceType();
        this.CombineOrderNumber = tasks.getCombinedOrderNumber();
        this.HelperResourceId = tasks.getHelper_Resource_Id();
        this.StandardProperty = tasks.getStandardProperty();
        this.customerInstructions = tasks.getCustomer_Instructions();
        this.IsTaskToBeHighlighted = tasks.getIsTaskToBeHighlighted();
        this.ColorForTheTaskToBeHighlighted = tasks.getColorForTheTaskToBeHighlighted();
        this.ReasonForHighlightedTask = tasks.getReasonForHighlightedTask();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(AccountName);
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
        dest.writeString(TaskAssignmentStartDate);
        dest.writeString(TaskAssignmentStartTime);
        dest.writeString(TaskAssignmentEndDate);
        dest.writeString(TaskAssignmentEndTime);
        dest.writeString(AssignmentStartDate);
        dest.writeString(AssignmentEndDate);
        dest.writeString(Status);
        dest.writeString(TaskId);
        dest.writeString(ServicePlan);
        dest.writeString(ServiceType);
        dest.writeString(primaryMobile);
        dest.writeString(altMobile);
        dest.writeString(techMobile);
        dest.writeString(AccountLat);
        dest.writeString(AccountLong);
        dest.writeString(OnsiteLat);
        dest.writeString(OnsiteLong);
        dest.writeString(CompletedLat);
        dest.writeString(CompletedLong);
        dest.writeString(CustomerLat);
        dest.writeString(CustomerLong);
        dest.writeString(Tag);
        dest.writeString(AccountType);
        dest.writeInt(SequenceNumber);
        dest.writeByte((byte) (IsDetailVisible == null ? 0 : IsDetailVisible ? 1 : 2));
        dest.writeByte((byte) (IsCombineTask == null ? 0 : IsCombineTask ? 1 : 2));
        dest.writeString(CombineTaskType);
        dest.writeString(CombineOrderNumber);
        dest.writeString(HelperResourceId);
        dest.writeString(customerInstructions);
        dest.writeByte((byte) (IsTaskToBeHighlighted == null ? 0 : IsTaskToBeHighlighted ? 1 : 2));
        dest.writeString(ColorForTheTaskToBeHighlighted);
        dest.writeString(ReasonForHighlightedTask);
    }
}


