package com.yummoidmkschinky.customerapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuitemDataItem{

	@SerializedName("item_img")
	private String itemImg;

	@SerializedName("price")
	private double price;

	@SerializedName("is_customize")
	private int isCustomize;

	@SerializedName("cdesc")
	private String cdesc;

	@SerializedName("is_quantity")
	private String isQuantity;

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	@SerializedName("is_veg")
	private int isVeg;

	@SerializedName("addondata")
	@Expose
	private List<Addondata> addondata = null;

	private String addonID;
	private String addonTitel;
	private String addonPrice;
	private int qty;
	private String rid;
	private String rtitel;
	private String rlocation;
	private String rimage;

	public String getItemImg(){
		return itemImg;
	}

	public double getPrice(){
		return price;
	}

	public int getIsCustomize(){
		return isCustomize;
	}

	public String getCdesc(){
		return cdesc;
	}

	public String getIsQuantity(){
		return isQuantity;
	}

	public String getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}

	public int getIsVeg(){
		return isVeg;
	}

	public List<Addondata> getAddondata() {
		return addondata;
	}

	public void setItemImg(String itemImg) {
		this.itemImg = itemImg;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setIsCustomize(int isCustomize) {
		this.isCustomize = isCustomize;
	}

	public void setCdesc(String cdesc) {
		this.cdesc = cdesc;
	}

	public void setIsQuantity(String isQuantity) {
		this.isQuantity = isQuantity;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setIsVeg(int isVeg) {
		this.isVeg = isVeg;
	}

	public void setAddondata(List<Addondata> addondata) {
		this.addondata = addondata;
	}

	public String getAddonID() {
		return addonID;
	}

	public void setAddonID(String addonID) {
		this.addonID = addonID;
	}

	public String getAddonTitel() {
		return addonTitel;
	}

	public void setAddonTitel(String addonTitel) {
		this.addonTitel = addonTitel;
	}

	public String getAddonPrice() {
		return addonPrice;
	}

	public void setAddonPrice(String addonPrice) {
		this.addonPrice = addonPrice;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getRtitel() {
		return rtitel;
	}

	public void setRtitel(String rtitel) {
		this.rtitel = rtitel;
	}

	public String getRlocation() {
		return rlocation;
	}

	public void setRlocation(String rlocation) {
		this.rlocation = rlocation;
	}

	public String getRimage() {
		return rimage;
	}

	public void setRimage(String rimage) {
		this.rimage = rimage;
	}
}