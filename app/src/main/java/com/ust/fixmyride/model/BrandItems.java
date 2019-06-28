package com.ust.fixmyride.model;

/**
 * Created by Bipul on 12-09-2016.
 */
public class BrandItems {
    private String brand_name;
    private boolean checked = false ;

    public BrandItems(){

    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
