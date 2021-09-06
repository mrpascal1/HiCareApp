package com.ab.hicarerun.network.models.TSScannerModel

import com.google.gson.annotations.SerializedName

data class Pest_Type (
    @SerializedName("Id") val id : Int?,
    @SerializedName("Barcode_Type") val barcode_Type : String?,
    @SerializedName("Sub_Type") val sub_Type : String?,
    @SerializedName("Show_Count") val show_Count : Boolean?,
    @SerializedName("Capture_Image") val capture_Image : Boolean?,
    @SerializedName("Pest_Count") val pest_Count : Int?,
    @SerializedName("Image_Url") val image_Url : String?
)
