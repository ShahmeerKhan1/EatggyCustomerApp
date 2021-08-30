package com.yummoidmkschinky.customerapp.model;

import com.google.gson.annotations.SerializedName;

public class CatlistItem{

	@SerializedName("cat_img")
	private String catImg;

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	public String getCatImg(){
		return catImg;
	}

	public String getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}
}