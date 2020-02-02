package com.ab.hicarerun.viewmodel;

import com.ab.hicarerun.network.models.AttachmentModel.GetAttachmentList;
import com.ab.hicarerun.network.models.ChemicalModel.Chemicals;

import java.util.HashMap;
import java.util.List;

public class ChemicalViewModel {
    private String id;
    private String name;
    private String consumption;
    private String standard;
    private String actual;
    private String edtActual;
    private HashMap<Integer, String> actualList;
    private String chemType;
    private String serviceArea;
    private String orignal;
    private Boolean isChemicalChanged;

    public ChemicalViewModel() {
        this.id = "NA";
        this.name = "NA";
        this.consumption = "NA";
        this.standard = "NA";
        this.actual = "NA";
        this.edtActual = "NA";
        this.chemType = "NA";
        this.serviceArea = "NA";
        this.orignal = "NA";
        this.isChemicalChanged = false;
    }


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

    public String getConsumption() {
        return consumption;
    }

    public void setConsumption(String consumption) {
        this.consumption = consumption;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public String getEdtActual() {
        return edtActual;
    }

    public void setEdtActual(String edtActual) {
        this.edtActual = edtActual;
    }

    public HashMap<Integer, String> getActualList() {
        return actualList;
    }

    public void setActualList(HashMap<Integer, String> actualList) {
        this.actualList = actualList;
    }

    public String getChemType() {
        return chemType;
    }

    public void setChemType(String chemType) {
        this.chemType = chemType;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public String getOrignal() {
        return orignal;
    }

    public void setOrignal(String orignal) {
        this.orignal = orignal;
    }

    public Boolean getChemicalChanged() {
        return isChemicalChanged;
    }

    public void setChemicalChanged(Boolean chemicalChanged) {
        isChemicalChanged = chemicalChanged;
    }

    public void clone(Chemicals chemicals) {
        this.id = chemicals.getId();
        this.name = chemicals.getCWFProductName();
        this.consumption = chemicals.getConsumption();
        this.standard = chemicals.getStandard_Usage();
        this.edtActual = chemicals.getActual_Usage();
        this.chemType = chemicals.getChemType();
        this.serviceArea = chemicals.getServiceArea();
        this.orignal = chemicals.getOrignal();
        this.isChemicalChanged = chemicals.getChemicalChanged();
    }

}
