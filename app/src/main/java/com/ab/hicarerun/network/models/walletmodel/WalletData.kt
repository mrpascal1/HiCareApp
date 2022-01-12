package com.ab.hicarerun.network.models.walletmodel

import com.google.gson.annotations.SerializedName

data class WalletData (
    @SerializedName("earnedList") val earnedList : List<EarnedList>?,
    @SerializedName("burnedList") val burnedList : List<BurnedList>?,
    @SerializedName("pointsInWallet") val pointsInWallet : Int?,
    @SerializedName("totalReedemablePercentageInWallet") val totalReedemablePercentageInWallet : Int?,
    @SerializedName("totalRedeemablePointsInWallet") val totalRedeemablePointsInWallet : Double?,
    @SerializedName("pointsEarned") val pointsEarned : Int?,
    @SerializedName("pointsBurned") val pointsBurned : Int?,
    @SerializedName("historyList") val historyList : List<HistoryList>?
)