package com.yummoidmkschinky.customerapp.model;

public class Tips {
    public int price;
    public boolean isSelect=false;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
