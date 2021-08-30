package com.yummoidmkschinky.customerapp.model;

import com.google.gson.annotations.SerializedName;

public class MainData{

	@SerializedName("pdboy")
	private String pdboy;

	@SerializedName("wname")
	private String wname;

	@SerializedName("one_key")
	private String oneKey;

	@SerializedName("timezone")
	private String timezone;

	@SerializedName("rcredit")
	private String rcredit;

	@SerializedName("webname")
	private String webname;

	@SerializedName("is_tip")
	private int isTip;

	@SerializedName("weblogo")
	private String weblogo;

	@SerializedName("d_key")
	private String dKey;

	@SerializedName("is_dmode")
	private String isDmode;

	@SerializedName("tax")
	private String tax;

	@SerializedName("one_hash")
	private String oneHash;

	@SerializedName("pstore")
	private String pstore;

	@SerializedName("is_tax")
	private int isTax;

	@SerializedName("d_hash")
	private String dHash;

	@SerializedName("currency")
	private String currency;

	@SerializedName("tip")
	private String tip;

	@SerializedName("id")
	private String id;

	@SerializedName("scredit")
	private String scredit;

	public String getPdboy(){
		return pdboy;
	}

	public String getWname(){
		return wname;
	}

	public String getOneKey(){
		return oneKey;
	}

	public String getTimezone(){
		return timezone;
	}

	public String getRcredit(){
		return rcredit;
	}

	public String getWebname(){
		return webname;
	}

	public int getIsTip(){
		return isTip;
	}

	public String getWeblogo(){
		return weblogo;
	}

	public String getDKey(){
		return dKey;
	}

	public String getIsDmode(){
		return isDmode;
	}

	public String getTax(){
		return tax;
	}

	public String getOneHash(){
		return oneHash;
	}

	public String getPstore(){
		return pstore;
	}

	public int getIsTax(){
		return isTax;
	}

	public String getDHash(){
		return dHash;
	}

	public String getCurrency(){
		return currency;
	}

	public String getTip(){
		return tip;
	}

	public String getId(){
		return id;
	}

	public String getScredit(){
		return scredit;
	}
}