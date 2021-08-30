package com.yummoidmkschinky.customerapp.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class HomeData {

	@SerializedName("wallet")
	private int wallet;

	@SerializedName("Main_Data")
	private MainData mainData;

	@SerializedName("Banner")
	private List<BannerItem> banner;

	@SerializedName("Catlist")
	private List<CatlistItem> catlist;

	@SerializedName("restuarant_data")
	private List<RestuarantDataItem> restuarantData;

	public int getWallet(){
		return wallet;
	}

	public MainData getMainData(){
		return mainData;
	}

	public List<BannerItem> getBanner(){
		return banner;
	}

	public List<CatlistItem> getCatlist(){
		return catlist;
	}

	public List<RestuarantDataItem> getRestuarantData(){
		return restuarantData;
	}
}