package com.yummoidmkschinky.customerapp.model;

import com.google.gson.annotations.SerializedName;

public class Home {

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("HomeData")
	private HomeData resultData;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("Result")
	private String result;

	public String getResponseCode(){
		return responseCode;
	}

	public HomeData getResultData(){
		return resultData;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public String getResult(){
		return result;
	}
}