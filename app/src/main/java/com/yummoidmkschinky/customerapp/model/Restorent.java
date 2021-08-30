package com.yummoidmkschinky.customerapp.model;

import com.google.gson.annotations.SerializedName;

public class Restorent{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("RestData")
	private RestorentData resultData;



	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("Result")
	private String result;

	public String getResponseCode(){
		return responseCode;
	}

	public RestorentData getResultData(){
		return resultData;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public String getResult(){
		return result;
	}


}