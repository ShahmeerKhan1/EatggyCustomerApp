package com.yummoidmkschinky.customerapp.model;

import com.google.gson.annotations.SerializedName;

public class RestuarantDataItem{

	@SerializedName("rest_charge")
	private String restCharge;

	@SerializedName("rest_dcharge")
	private String restDcharge;

	@SerializedName("rest_costfortwo")
	private String restCostfortwo;

	@SerializedName("rest_distance")
	private String restDistance;

	@SerializedName("rest_id")
	private String restId;

	@SerializedName("rest_img")
	private String restImg;

	@SerializedName("rest_title")
	private String restTitle;

	@SerializedName("rest_rating")
	private String restRating;

	@SerializedName("rest_morder")
	private String restMorder;

	@SerializedName("rest_sdesc")
	private String restSdesc;

	@SerializedName("rest_deliverytime")
	private String restDeliverytime;

	@SerializedName("rest_is_veg")
	private int restIsVeg;

	@SerializedName("rest_full_address")
	private String restFullAddress;

	@SerializedName("cou_title")
	private String couTitle;

	@SerializedName("cou_subtitle")
	private String couSubtitle;

	@SerializedName("rest_landmark")
	private String restLandmark;

	@SerializedName("IS_FAVOURITE")
	private int ISFAVOURITE;

	@SerializedName("rest_licence")
	private String restLicence;

	@SerializedName("rest_is_open")
	private int restIsopen;

	public String getRestCharge(){
		return restCharge;
	}

	public String getRestDcharge(){
		return restDcharge;
	}

	public String getRestCostfortwo(){
		return restCostfortwo;
	}

	public String getRestDistance(){
		return restDistance;
	}

	public String getRestId(){
		return restId;
	}

	public String getRestImg(){
		return restImg;
	}

	public String getRestTitle(){
		return restTitle;
	}

	public String getRestRating(){
		return restRating;
	}

	public String getRestMorder(){
		return restMorder;
	}

	public String getRestSdesc(){
		return restSdesc;
	}

	public String getRestDeliverytime(){
		return restDeliverytime;
	}

	public int getRestIsVeg(){
		return restIsVeg;
	}

	public String getRestFullAddress(){
		return restFullAddress;
	}

	public String getCouTitle() {
		return couTitle;
	}

	public void setCouTitle(String couTitle) {
		this.couTitle = couTitle;
	}

	public String getCouSubtitle() {
		return couSubtitle;
	}

	public void setCouSubtitle(String couSubtitle) {
		this.couSubtitle = couSubtitle;
	}

	public String getRestLandmark() {
		return restLandmark;
	}

	public void setRestLandmark(String restLandmark) {
		this.restLandmark = restLandmark;
	}

	public String getRestLicence() {
		return restLicence;
	}

	public void setRestLicence(String restLicence) {
		this.restLicence = restLicence;
	}

	public int getISFAVOURITE() {
		return ISFAVOURITE;
	}

	public void setISFAVOURITE(int ISFAVOURITE) {
		this.ISFAVOURITE = ISFAVOURITE;
	}

	public int getRestIsopen() {
		return restIsopen;
	}

	public void setRestIsopen(int restIsopen) {
		this.restIsopen = restIsopen;
	}
}