package com.yummoidmkschinky.customerapp.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OrderInfo{

	@SerializedName("rest_charge")
	private String restCharge;

	@SerializedName("wall_amt")
	private String wallAmt;

	@SerializedName("coupon_title")
	private String couponTitle;

	@SerializedName("p_method_name")
	private String pMethodName;

	@SerializedName("cou_amt")
	private String couAmt;

	@SerializedName("o_status")
	private String oStatus;

	@SerializedName("order_complete_date")
	private String orderCompleteDate;

	@SerializedName("address_type")
	private String addressType;

	@SerializedName("rider_tip")
	private String riderTip;

	@SerializedName("cust_address")
	private String custAddress;

	@SerializedName("tax")
	private String tax;

	@SerializedName("rest_name")
	private String restName;

	@SerializedName("delivery_charge")
	private String deliveryCharge;

	@SerializedName("rest_address")
	private String restAddress;

	@SerializedName("subtotal")
	private String subtotal;

	@SerializedName("rider_name")
	private String riderName;

	@SerializedName("order_total")
	private String orderTotal;

	@SerializedName("order_id")
	private String orderId;

	@SerializedName("order_items")
	private List<OrderItemsItem> orderItems;

	public String getRestCharge(){
		return restCharge;
	}

	public String getWallAmt(){
		return wallAmt;
	}

	public String getCouponTitle(){
		return couponTitle;
	}

	public String getPMethodName(){
		return pMethodName;
	}

	public String getCouAmt(){
		return couAmt;
	}

	public String getOStatus(){
		return oStatus;
	}

	public String getOrderCompleteDate(){
		return orderCompleteDate;
	}

	public String getAddressType(){
		return addressType;
	}

	public String getRiderTip(){
		return riderTip;
	}

	public String getCustAddress(){
		return custAddress;
	}

	public String getTax(){
		return tax;
	}

	public String getRestName(){
		return restName;
	}

	public String getDeliveryCharge(){
		return deliveryCharge;
	}

	public String getRestAddress(){
		return restAddress;
	}

	public String getSubtotal(){
		return subtotal;
	}

	public String getRiderName(){
		return riderName;
	}

	public String getOrderTotal(){
		return orderTotal;
	}

	public String getOrderId(){
		return orderId;
	}

	public List<OrderItemsItem> getOrderItems(){
		return orderItems;
	}
}