package com.ab.hicarerun.network.models.voucher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VoucherResponse {

    @SerializedName("ReferralCode")
    @Expose
    private String ReferralCode;
    @SerializedName("ImageUrl")
    @Expose
    private String ImageUrl;

    @SerializedName("Title")
    @Expose
    private String Title;

    @SerializedName("Description")
    @Expose
    private String Description;

    @SerializedName("ShareText")
    @Expose
    private String ShareText;

    public String getReferralCode() {
        return ReferralCode;
    }

    public void setReferralCode(String referralCode) {
        ReferralCode = referralCode;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getShareText() {
        return ShareText;
    }

    public void setShareText(String shareText) {
        ShareText = shareText;
    }
}
