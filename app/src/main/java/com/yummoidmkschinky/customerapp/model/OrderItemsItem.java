package com.yummoidmkschinky.customerapp.model;

import com.google.gson.annotations.SerializedName;

public class OrderItemsItem{

	@SerializedName("item_addon")
	private String itemAddon;

	@SerializedName("item_name")
	private String itemName;

	@SerializedName("item_total")
	private int itemTotal;

	@SerializedName("is_veg")
	private String isVeg;

	public String getItemAddon(){
		return itemAddon;
	}

	public String getItemName(){
		return itemName;
	}

	public int getItemTotal(){
		return itemTotal;
	}

	public String getIsVeg(){
		return isVeg;
	}
}