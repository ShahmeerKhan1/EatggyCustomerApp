package com.yummoidmkschinky.customerapp.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Order{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("OrderHistory")
	private List<OrderHistoryItem> orderHistory;

	@SerializedName("Result")
	private String result;

	public String getResponseCode(){
		return responseCode;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public List<OrderHistoryItem> getOrderHistory(){
		return orderHistory;
	}

	public String getResult(){
		return result;
	}
}