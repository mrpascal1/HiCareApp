package com.ab.hicarerun.network.models.TSScannerModel

import com.google.gson.annotations.SerializedName

data class BarcodeDDPestType(
    @SerializedName("Id") val id : Int?,
    @SerializedName("Barcode_Type") val barcode_Type : String?,
    @SerializedName("Sub_Type") val sub_Type : String?,
    @SerializedName("Show_Count") val show_Count : Boolean?,
    @SerializedName("Capture_Image") val capture_Image : Boolean?,
    @SerializedName("Show_Option") val show_Option : Boolean?,
    @SerializedName("Option_Value") val option_Value : String?,
    @SerializedName("Option_List") val option_List : List<Option_List>?,
    @SerializedName("Pest_Count") var pest_Count : String?,
    @SerializedName("Image_Url") var image_Url : String?,
    val barcodeId : Int?,
)
