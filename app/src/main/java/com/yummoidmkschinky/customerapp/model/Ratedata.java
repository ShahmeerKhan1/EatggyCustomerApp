package com.yummoidmkschinky.customerapp.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Ratedata{

	@SerializedName("rider_image")
	private String riderImage;

	@SerializedName("order_complete_date")
	private String orderCompleteDate;

	@SerializedName("rest_image")
	private String restImage;

	@SerializedName("rider_name")
	private String riderName;

	@SerializedName("rest_name")
	private String restName;

	@SerializedName("order_id")
	private String orderId;

	@SerializedName("order_items")
	private List<OrderItemsItem> orderItems;

	public String getRiderImage(){
		return riderImage;
	}

	public String getOrderCompleteDate(){
		return orderCompleteDate;
	}

	public String getRestImage(){
		return restImage;
	}

	public String getRiderName(){
		return riderName;
	}

	public String getRestName(){
		return restName;
	}

	public String getOrderId(){
		return orderId;
	}

	public List<OrderItemsItem> getOrderItems(){
		return orderItems;
	}
}