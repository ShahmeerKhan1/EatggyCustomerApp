package com.yummoidmkschinky.customerapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Addondata {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("addon_is_radio")
    @Expose
    private int addonIsRadio;
    @SerializedName("addon_is_quantity")
    @Expose
    private int addonIsQuantity;
    @SerializedName("addon_limit")
    @Expose
    private int addonLimit;
    @SerializedName("addon_is_required")
    @Expose
    private String addonIsRequired;
    @SerializedName("addon_item_data")
    @Expose
    private List<AddonItem> addonItemData = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAddonIsRadio() {
        return addonIsRadio;
    }

    public void setAddonIsRadio(int addonIsRadio) {
        this.addonIsRadio = addonIsRadio;
    }

    public int getAddonIsQuantity() {
        return addonIsQuantity;
    }

    public void setAddonIsQuantity(int addonIsQuantity) {
        this.addonIsQuantity = addonIsQuantity;
    }

    public int getAddonLimit() {
        return addonLimit;
    }

    public void setAddonLimit(int addonLimit) {
        this.addonLimit = addonLimit;
    }

    public String getAddonIsRequired() {
        return addonIsRequired;
    }

    public void setAddonIsRequired(String addonIsRequired) {
        this.addonIsRequired = addonIsRequired;
    }

    public List<AddonItem> getAddonItemData() {
        return addonItemData;
    }

    public void setAddonItemData(List<AddonItem> addonItemData) {
        this.addonItemData = addonItemData;
    }

}
