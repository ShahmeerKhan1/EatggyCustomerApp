package com.yummoidmkschinky.customerapp.model;

import com.google.gson.annotations.SerializedName;

public class BannerItem{

	@SerializedName("img")
	private String img;

	@SerializedName("id")
	private String id;

	public String getImg(){
		return img;
	}

	public String getId(){
		return id;
	}
}