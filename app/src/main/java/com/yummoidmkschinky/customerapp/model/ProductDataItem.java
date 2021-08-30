package com.yummoidmkschinky.customerapp.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ProductDataItem{

	@SerializedName("Menuitem_Data")
	private List<MenuitemDataItem> menuitemData;

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	public List<MenuitemDataItem> getMenuitemData(){
		return menuitemData;
	}

	public String getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}
}