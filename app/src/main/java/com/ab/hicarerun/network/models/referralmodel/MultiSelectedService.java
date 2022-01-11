package com.ab.hicarerun.network.models.referralmodel;

/**
 * Created by Arjun Bhatt on 7/9/2020.
 */
public class MultiSelectedService {
    private String name;
    private boolean selected;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
