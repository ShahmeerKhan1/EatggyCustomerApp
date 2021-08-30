package com.yummoidmkschinky.customerapp.model;

import com.google.gson.annotations.SerializedName;

public class OrderDetail{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("OrderInfo")
	private OrderInfo orderInfo;

	@SerializedName("Result")
	private String result;

	public String getResponseCode(){
		return responseCode;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public OrderInfo getOrderInfo(){
		return orderInfo;
	}

	public String getResult(){
		return result;
	}
}