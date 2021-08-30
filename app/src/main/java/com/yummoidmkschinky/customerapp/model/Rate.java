package com.yummoidmkschinky.customerapp.model;

import com.google.gson.annotations.SerializedName;

public class Rate{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("ratedata")
	private Ratedata ratedata;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("Result")
	private String result;

	public String getResponseCode(){
		return responseCode;
	}

	public Ratedata getRatedata(){
		return ratedata;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public String getResult(){
		return result;
	}
}