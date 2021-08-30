package com.yummoidmkschinky.customerapp.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RestorentData {

	@SerializedName("Coupon")
	private List<CouponItem> coupon;

	@SerializedName("Product_Data")
	private List<ProductDataItem> productData;

	@SerializedName("restuarant_data")
	private List<RestuarantDataItem> restuarantData;

	public List<CouponItem> getCoupon(){
		return coupon;
	}

	public List<ProductDataItem> getProductData(){
		return productData;
	}

	public List<RestuarantDataItem> getRestuarantData(){
		return restuarantData;
	}
}