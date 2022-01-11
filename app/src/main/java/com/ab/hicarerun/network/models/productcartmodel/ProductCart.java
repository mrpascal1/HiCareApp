package com.ab.hicarerun.network.models.productcartmodel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Arjun Bhatt on 5/29/2020.
 */
public class ProductCart extends RealmObject {
    @PrimaryKey
    private int Id;
    private String SpCode;
    private String Quantity;
    private String ImgURL;
    private String Title;
    private String Unit;
    private Double DiscountedAmount;
    private Double ActualAmount;
    private Double Discount;

    public ProductCart() {
        Id = 0;
        SpCode = "NA";
        Quantity = "0";
        ImgURL = "NA";
        Title = "NA";
        Unit = "NA";
        DiscountedAmount = 0.0;
        ActualAmount = 0.0;
        Discount = 0.0;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getSpCode() {
        return SpCode;
    }

    public void setSpCode(String spCode) {
        SpCode = spCode;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getImgURL() {
        return ImgURL;
    }

    public void setImgURL(String imgURL) {
        ImgURL = imgURL;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public Double getDiscountedAmount() {
        return DiscountedAmount;
    }

    public void setDiscountedAmount(Double discountedAmount) {
        DiscountedAmount = discountedAmount;
    }

    public Double getActualAmount() {
        return ActualAmount;
    }

    public void setActualAmount(Double actualAmount) {
        ActualAmount = actualAmount;
    }

    public Double getDiscount() {
        return Discount;
    }

    public void setDiscount(Double discount) {
        Discount = discount;
    }
}
