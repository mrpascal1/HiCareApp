package com.ab.hicarerun.network.models.profilemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 7/26/2019.
 */
public class Profile {
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("UserPassword")
    @Expose
    private String userPassword;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("ProfileId")
    @Expose
    private Integer profileId;
    @SerializedName("ProfileName")
    @Expose
    private String profileName;
    @SerializedName("Role")
    @Expose
    private String role;
    @SerializedName("Permissions")
    @Expose
    private String permissions;
    @SerializedName("ApplicationStringName")
    @Expose
    private String applicationStringName;
    @SerializedName("ActionName")
    @Expose
    private String actionName;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;
    @SerializedName("CreatedBy")
    @Expose
    private Integer createdBy;
    @SerializedName("ModifiedOn")
    @Expose
    private String modifiedOn;
    @SerializedName("ModifiedBy")
    @Expose
    private Integer modifiedBy;
    @SerializedName("AllowedIpAddress")
    @Expose
    private String allowedIpAddress;
    @SerializedName("IpModel")
    @Expose
    private List<String> ipModel = null;
    @SerializedName("ClientIpAddress")
    @Expose
    private String clientIpAddress;
    @SerializedName("ProfilePic")
    @Expose
    private String profilePic;
    @SerializedName("EmployeeCode")
    @Expose
    private String employeeCode;
    @SerializedName("Aadhar_No")
    @Expose
    private String aadharNo;
    @SerializedName("Blood_Group")
    @Expose
    private String bloodGroup;
    @SerializedName("Emergency_Contact_No")
    @Expose
    private String emergencyContactNo;
    @SerializedName("ServiceCenter")
    @Expose
    private String serviceCenter;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("Supplements")
    @Expose
    private String supplements;
    @SerializedName("Temperature")
    @Expose
    private String temperature;
    @SerializedName("Oxymeter")
    @Expose
    private String oxymeter;
    @SerializedName("Pulse")
    @Expose
    private String pulse;
    @SerializedName("Symtoms")
    @Expose
    private String symtoms;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getApplicationStringName() {
        return applicationStringName;
    }

    public void setApplicationStringName(String applicationStringName) {
        this.applicationStringName = applicationStringName;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getAllowedIpAddress() {
        return allowedIpAddress;
    }

    public void setAllowedIpAddress(String allowedIpAddress) {
        this.allowedIpAddress = allowedIpAddress;
    }

    public List<String> getIpModel() {
        return ipModel;
    }

    public void setIpModel(List<String> ipModel) {
        this.ipModel = ipModel;
    }

    public String getClientIpAddress() {
        return clientIpAddress;
    }

    public void setClientIpAddress(String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getEmergencyContactNo() {
        return emergencyContactNo;
    }

    public void setEmergencyContactNo(String emergencyContactNo) {
        this.emergencyContactNo = emergencyContactNo;
    }

    public String getServiceCenter() {
        return serviceCenter;
    }

    public void setServiceCenter(String serviceCenter) {
        this.serviceCenter = serviceCenter;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSupplements() {
        return supplements;
    }

    public void setSupplements(String supplements) {
        this.supplements = supplements;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getOxymeter() {
        return oxymeter;
    }

    public void setOxymeter(String oxymeter) {
        this.oxymeter = oxymeter;
    }

    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    public String getSymtoms() {
        return symtoms;
    }

    public void setSymtoms(String symtoms) {
        this.symtoms = symtoms;
    }




}
