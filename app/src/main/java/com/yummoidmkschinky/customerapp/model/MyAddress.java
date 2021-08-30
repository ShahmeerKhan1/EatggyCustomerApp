package com.yummoidmkschinky.customerapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyAddress {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("hno")
    @Expose
    private String hno;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("lat_map")
    @Expose
    private String latMap;
    @SerializedName("long_map")
    @Expose
    private String longMap;
    @SerializedName("landmark")
    @Expose
    private String landmark;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("address_image")
    @Expose
    private String addressImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHno() {
        return hno;
    }

    public void setHno(String hno) {
        this.hno = hno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatMap() {
        return latMap;
    }

    public void setLatMap(String latMap) {
        this.latMap = latMap;
    }

    public String getLongMap() {
        return longMap;
    }

    public void setLongMap(String longMap) {
        this.longMap = longMap;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddressImage() {
        return addressImage;
    }

    public void setAddressImage(String addressImage) {
        this.addressImage = addressImage;
    }

}
