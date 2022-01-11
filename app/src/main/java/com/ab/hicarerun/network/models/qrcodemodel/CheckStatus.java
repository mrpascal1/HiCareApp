package com.ab.hicarerun.network.models.qrcodemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 7/31/2020.
 */
public class CheckStatus {
    @SerializedName("TXNID")
    @Expose
    private String tXNID;
    @SerializedName("BANKTXNID")
    @Expose
    private String bANKTXNID;
    @SerializedName("ORDERID")
    @Expose
    private String oRDERID;
    @SerializedName("TXNAMOUNT")
    @Expose
    private String tXNAMOUNT;
    @SerializedName("STATUS")
    @Expose
    private String sTATUS;
    @SerializedName("TXNTYPE")
    @Expose
    private String tXNTYPE;
    @SerializedName("GATEWAYNAME")
    @Expose
    private String gATEWAYNAME;
    @SerializedName("RESPCODE")
    @Expose
    private String rESPCODE;
    @SerializedName("RESPMSG")
    @Expose
    private String rESPMSG;
    @SerializedName("BANKNAME")
    @Expose
    private String bANKNAME;
    @SerializedName("MID")
    @Expose
    private String mID;
    @SerializedName("PAYMENTMODE")
    @Expose
    private String pAYMENTMODE;
    @SerializedName("REFUNDAMT")
    @Expose
    private String rEFUNDAMT;
    @SerializedName("TXNDATE")
    @Expose
    private String tXNDATE;
    @SerializedName("MERC_UNQ_REF")
    @Expose
    private Object mERCUNQREF;
    @SerializedName("POS_ID")
    @Expose
    private Object pOSID;
    @SerializedName("UDF_1")
    @Expose
    private Object uDF1;
    @SerializedName("UDF_2")
    @Expose
    private Object uDF2;
    @SerializedName("UDF_3")
    @Expose
    private Object uDF3;

    public String getTXNID() {
        return tXNID;
    }

    public void setTXNID(String tXNID) {
        this.tXNID = tXNID;
    }

    public String getBANKTXNID() {
        return bANKTXNID;
    }

    public void setBANKTXNID(String bANKTXNID) {
        this.bANKTXNID = bANKTXNID;
    }

    public String getORDERID() {
        return oRDERID;
    }

    public void setORDERID(String oRDERID) {
        this.oRDERID = oRDERID;
    }

    public String getTXNAMOUNT() {
        return tXNAMOUNT;
    }

    public void setTXNAMOUNT(String tXNAMOUNT) {
        this.tXNAMOUNT = tXNAMOUNT;
    }

    public String getSTATUS() {
        return sTATUS;
    }

    public void setSTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }

    public String getTXNTYPE() {
        return tXNTYPE;
    }

    public void setTXNTYPE(String tXNTYPE) {
        this.tXNTYPE = tXNTYPE;
    }

    public String getGATEWAYNAME() {
        return gATEWAYNAME;
    }

    public void setGATEWAYNAME(String gATEWAYNAME) {
        this.gATEWAYNAME = gATEWAYNAME;
    }

    public String getRESPCODE() {
        return rESPCODE;
    }

    public void setRESPCODE(String rESPCODE) {
        this.rESPCODE = rESPCODE;
    }

    public String getRESPMSG() {
        return rESPMSG;
    }

    public void setRESPMSG(String rESPMSG) {
        this.rESPMSG = rESPMSG;
    }

    public String getBANKNAME() {
        return bANKNAME;
    }

    public void setBANKNAME(String bANKNAME) {
        this.bANKNAME = bANKNAME;
    }

    public String getMID() {
        return mID;
    }

    public void setMID(String mID) {
        this.mID = mID;
    }

    public String getPAYMENTMODE() {
        return pAYMENTMODE;
    }

    public void setPAYMENTMODE(String pAYMENTMODE) {
        this.pAYMENTMODE = pAYMENTMODE;
    }

    public String getREFUNDAMT() {
        return rEFUNDAMT;
    }

    public void setREFUNDAMT(String rEFUNDAMT) {
        this.rEFUNDAMT = rEFUNDAMT;
    }

    public String getTXNDATE() {
        return tXNDATE;
    }

    public void setTXNDATE(String tXNDATE) {
        this.tXNDATE = tXNDATE;
    }

    public Object getMERCUNQREF() {
        return mERCUNQREF;
    }

    public void setMERCUNQREF(Object mERCUNQREF) {
        this.mERCUNQREF = mERCUNQREF;
    }

    public Object getPOSID() {
        return pOSID;
    }

    public void setPOSID(Object pOSID) {
        this.pOSID = pOSID;
    }

    public Object getUDF1() {
        return uDF1;
    }

    public void setUDF1(Object uDF1) {
        this.uDF1 = uDF1;
    }

    public Object getUDF2() {
        return uDF2;
    }

    public void setUDF2(Object uDF2) {
        this.uDF2 = uDF2;
    }

    public Object getUDF3() {
        return uDF3;
    }

    public void setUDF3(Object uDF3) {
        this.uDF3 = uDF3;
    }
}
