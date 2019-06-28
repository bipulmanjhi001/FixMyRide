package com.ust.fixmyride.model;

/**
 * Created by Bipul on 22-09-2016.
 */
public class ProductItems {
    private String product_name;
    private boolean checked = false ;

    public ProductItems(){

    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}

