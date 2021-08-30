package com.yummoidmkschinky.customerapp.model;

import com.google.gson.annotations.SerializedName;

public class Mapinfo{

	@SerializedName("cust_address_lat")
	private double custAddressLat;

	@SerializedName("rest_lats")
	private double restLats;

	@SerializedName("rider_longs")
	private double riderLongs;

	@SerializedName("orderid")
	private String orderid;

	@SerializedName("order_arrive_seconds")
	private int orderArriveSeconds;

	@SerializedName("order_step")
	private int orderStep;

	@SerializedName("rest_name")
	private String restName;

	@SerializedName("rest_msg")
	private String restMsg;

	@SerializedName("rider_msg")
	private String riderMsg;

	@SerializedName("arrive_time")
	private String arriveTime;

	@SerializedName("rest_img")
	private String restImg;

	@SerializedName("cust_address_long")
	private double custAddressLong;

	@SerializedName("rider_name")
	private String riderName;

	@SerializedName("cust_address_type")
	private String custAddressType;

	@SerializedName("rest_mobile")
	private String restMobile;

	@SerializedName("rest_longs")
	private double restLongs;

	@SerializedName("rider_mobile")
	private String riderMobile;

	@SerializedName("rider_lats")
	private double riderLats;
	@SerializedName("rider_img")
	private String riderImg;

	public double getCustAddressLat(){
		return custAddressLat;
	}

	public double getRestLats(){
		return restLats;
	}

	public double getRiderLongs(){
		return riderLongs;
	}

	public String getOrderid(){
		return orderid;
	}

	public int getOrderArriveSeconds(){
		return orderArriveSeconds;
	}

	public int getOrderStep(){
		return orderStep;
	}

	public String getRestName(){
		return restName;
	}

	public String getRestMsg() {
		return restMsg;
	}

	public String getRiderMsg() {
		return riderMsg;
	}

	public String getArriveTime(){
		return arriveTime;
	}

	public String getRestImg(){
		return restImg;
	}

	public double getCustAddressLong(){
		return custAddressLong;
	}

	public String getRiderName(){
		return riderName;
	}

	public String getCustAddressType(){
		return custAddressType;
	}

	public String getRestMobile(){
		return restMobile;
	}

	public double getRestLongs(){
		return restLongs;
	}

	public String getRiderMobile(){
		return riderMobile;
	}

	public double getRiderLats(){
		return riderLats;
	}

	public String getRiderImg() {
		return riderImg;
	}
}