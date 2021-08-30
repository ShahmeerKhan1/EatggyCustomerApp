package com.yummoidmkschinky.customerapp.model;

import com.google.gson.annotations.SerializedName;

public class OrderHistoryItem{

	@SerializedName("o_status")
	private String oStatus;

	@SerializedName("order_complete_date")
	private String orderCompleteDate;

	@SerializedName("rest_rate")
	private int restRate;

	@SerializedName("rest_text")
	private String restText;

	@SerializedName("rider_text")
	private String riderText;

	@SerializedName("order_total")
	private String orderTotal;

	@SerializedName("rest_name")
	private String restName;

	@SerializedName("rider_rate")
	private int riderRate;

	@SerializedName("order_id")
	private String orderId;

	@SerializedName("rest_landmark")
	private String restLandmark;

	@SerializedName("order_items")
	private String orderItems;

	public String getOStatus(){
		return oStatus;
	}

	public String getOrderCompleteDate(){
		return orderCompleteDate;
	}

	public int getRestRate(){
		return restRate;
	}

	public String getRestText(){
		return restText;
	}

	public String getRiderText(){
		return riderText;
	}

	public String getOrderTotal(){
		return orderTotal;
	}

	public String getRestName(){
		return restName;
	}

	public int getRiderRate(){
		return riderRate;
	}

	public String getOrderId(){
		return orderId;
	}

	public String getRestLandmark(){
		return restLandmark;
	}

	public String getOrderItems(){
		return orderItems;
	}
}